package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.*;
import com.example.demo.DTO.FacturaConsultaResponseDTO.CajeroDTO;
import com.example.demo.DTO.FacturaConsultaResponseDTO.ClienteDTO;
import com.example.demo.DTO.FacturaConsultaResponseDTO.ProductoDTO;
import com.example.demo.entities.*;
import com.example.demo.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FacturaService {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ProductoRepository productoRepository;
	@Autowired
	private TipoPagoRepository tipoPagoRepository;
	@Autowired
	private TiendaRepository tiendaRepository;
	@Autowired
	private VendedorRepository vendedorRepository;
	@Autowired
	private CajeroRepository cajeroRepository;
	@Autowired
	private CompraRepository compraRepository;
	@Autowired
	private DetallesCompraRepository detallesCompraRepository;
	@Autowired
	private PagoRepository pagoRepository;
	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	public FacturaResponseDTO createFactura(String tiendaUuid, FacturaRequestDTO request) {
		// 1. Validar si la tienda existe
		UUID uuid = UUID.fromString(tiendaUuid);
		Tienda tienda = tiendaRepository.findByUuid(uuid)
				.orElseThrow(() -> new IllegalArgumentException("La tienda no existe"));

		// 2. Validar el cliente
		Cliente cliente = clienteRepository
				.findByDocumentoAndTipoDocumento(request.getCliente().getDocumento(),
						request.getCliente().getTipoDocumento()) // Aquí se pasa el tipoDocumento como String
				.orElseGet(() -> {
					// Registrar el cliente si no existe
					Cliente nuevoCliente = new Cliente();
					nuevoCliente.setDocumento(request.getCliente().getDocumento());
					nuevoCliente.setNombre(request.getCliente().getNombre());

					// Buscar el tipo de documento en el repositorio
					TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNombre(request.getCliente().getTipoDocumento());

					nuevoCliente.setTipoDocumento(tipoDocumento);
					return clienteRepository.save(nuevoCliente);
				});

		// 3. Validar el vendedor
		Vendedor vendedor = vendedorRepository.findByDocumento(request.getVendedor().getDocumento())
				.orElseThrow(() -> new IllegalArgumentException("El vendedor no existe en la tienda"));

		// 4. Validar el cajero
		Cajero cajero = cajeroRepository.findByToken(request.getCajero().getToken()).orElseThrow(
				() -> new IllegalArgumentException("El token no corresponde a ningún cajero en la tienda"));

		if (!cajero.getTienda().equals(tienda)) {
			throw new IllegalArgumentException("El cajero no está asignado a esta tienda");
		}

		// 5. Validar productos
		double total = 0.0;
		for (FacturaRequestDTO.ProductoDTO productoDTO : request.getProductos()) {
			Producto producto = productoRepository.findByReferencia(productoDTO.getReferencia())
					.orElseThrow(() -> new IllegalArgumentException(
							"La referencia del producto " + productoDTO.getReferencia() + " no existe"));

			if (producto.getCantidad() < productoDTO.getCantidad()) {
				throw new IllegalArgumentException("La cantidad a comprar supera el máximo del producto en tienda");
			}

			double subtotal = producto.getPrecio() * productoDTO.getCantidad()
					- (producto.getPrecio() * productoDTO.getCantidad() * productoDTO.getDescuento() / 100);
			total += subtotal;

			producto.setCantidad(producto.getCantidad() - productoDTO.getCantidad());
			productoRepository.save(producto);
		}

		// 6. Validar medios de pago
		double totalPagos = 0.0;
		if (request.getMediosPago().isEmpty()) {
			throw new IllegalArgumentException("No hay medios de pagos asignados para esta compra");
		}
		for (FacturaRequestDTO.MedioPagoDTO medioPagoDTO : request.getMediosPago()) {
			TipoPago tipoPago = tipoPagoRepository.findByNombre(medioPagoDTO.getTipoPago())
					.orElseThrow(() -> new IllegalArgumentException("Tipo de pago no permitido en la tienda"));

			totalPagos += medioPagoDTO.getValor();
		}

		if (total != totalPagos) {
			throw new IllegalArgumentException("El valor de la factura no coincide con el valor total de los pagos");
		}

		// 7. Crear la factura (compra)
		Compra compra = new Compra();
		compra.setCliente(cliente);
		compra.setTienda(tienda);
		compra.setVendedor(vendedor);
		compra.setCajero(cajero);
		compra.setTotal(total);
		compra.setImpuestos(request.getImpuesto());
		compra.setFecha(LocalDateTime.now());
		Compra facturaGuardada = compraRepository.save(compra);

		// 8. Guardar detalles de compra
		for (FacturaRequestDTO.ProductoDTO productoDTO : request.getProductos()) {
			Producto producto = productoRepository.findByReferencia(productoDTO.getReferencia()).get();

			DetallesCompra detallesCompra = new DetallesCompra();
			detallesCompra.setCompra(facturaGuardada);
			detallesCompra.setProducto(producto);
			detallesCompra.setCantidad(productoDTO.getCantidad());
			detallesCompra.setPrecio(producto.getPrecio());
			detallesCompra.setDescuento(productoDTO.getDescuento());

			detallesCompraRepository.save(detallesCompra);
		}

		// 9. Guardar pagos
		for (FacturaRequestDTO.MedioPagoDTO medioPagoDTO : request.getMediosPago()) {
			Pago pago = new Pago();
			pago.setCompra(facturaGuardada);
			pago.setTipoPago(tipoPagoRepository.findByNombre(medioPagoDTO.getTipoPago()).get());
			pago.setTarjetaTipo(medioPagoDTO.getTipoTarjeta());
			pago.setCuotas(medioPagoDTO.getCuotas());
			pago.setValor(medioPagoDTO.getValor());

			pagoRepository.save(pago);
		}

		// 10. Retornar respuesta
		FacturaResponseDTO response = new FacturaResponseDTO();
		response.setStatus("success");
		response.setMessage("La factura se ha creado correctamente con el número: " + facturaGuardada.getId());
		FacturaResponseDTO.FacturaData data = new FacturaResponseDTO.FacturaData();
		data.setNumero(facturaGuardada.getId());
		data.setTotal(total);
		data.setFecha(facturaGuardada.getFecha());
		response.setData(data);

		return response;
	}

	public FacturaConsultaResponseDTO consultarFactura(String tiendaUuid, FacturaConsultaRequestDTO request) {
	    // Validar el token del cajero
	    Cajero cajero = cajeroRepository.findByToken(request.getToken()).orElseThrow(
	            () -> new IllegalArgumentException("El token no corresponde a ningún cajero en la tienda"));

	    // Validar la factura
	    Compra compra = compraRepository.findById(request.getFactura())
	            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

	    if (!compra.getCajero().equals(cajero)) {
	        throw new IllegalArgumentException("El cajero no tiene permisos para consultar esta factura");
	    }

	    // Validar el cliente
	    Cliente cliente = clienteRepository
	            .findByDocumentoAndTipoDocumento(request.getCliente(),
	                    compra.getCliente().getTipoDocumento().getNombre())  // Usamos el nombre del tipoDocumento como String
	            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

	    // Construir la respuesta
	    FacturaConsultaResponseDTO response = new FacturaConsultaResponseDTO();
	    response.setTotal(compra.getTotal());
	    response.setImpuestos(compra.getImpuestos());

	    // Convertir cliente a ClienteDTO
	    ClienteDTO clienteDTO = new ClienteDTO();
	    clienteDTO.setDocumento(cliente.getDocumento());
	    clienteDTO.setNombre(cliente.getNombre());
	    clienteDTO.setTipoDocumento(cliente.getTipoDocumento().getNombre()); // Aquí se convierte el tipoDocumento a String
	    response.setCliente(clienteDTO);

	    // Obtener los detalles de compra desde el repositorio
	    List<DetallesCompra> detallesCompraList = detallesCompraRepository.findByCompra(compra);

	    // Agregar productos
	    List<ProductoDTO> productos = new ArrayList<>();
	    for (DetallesCompra detalles : detallesCompraList) {
	        ProductoDTO productoDTO = new ProductoDTO();
	        productoDTO.setReferencia(detalles.getProducto().getReferencia());
	        productoDTO.setNombre(detalles.getProducto().getNombre());
	        productoDTO.setCantidad(detalles.getCantidad());
	        productoDTO.setPrecio(detalles.getPrecio());
	        productoDTO.setDescuento(detalles.getDescuento());
	        productoDTO.setSubtotal(detalles.getPrecio() * detalles.getCantidad()
	                - (detalles.getPrecio() * detalles.getCantidad() * detalles.getDescuento() / 100));
	        productos.add(productoDTO);
	    }

	    response.setProductos(productos);

	    // Convertir cajero a CajeroDTO
	    CajeroDTO cajeroDTO = new CajeroDTO();
	    cajeroDTO.setDocumento(cajero.getDocumento());
	    cajeroDTO.setNombre(cajero.getNombre());
	    response.setCajero(cajeroDTO);

	    return response;
	}
}
