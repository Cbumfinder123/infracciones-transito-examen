package edu.pe.cibertec.infracciones;
import org.mockito.ArgumentCaptor;
import edu.pe.cibertec.infracciones.model.Pago;
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import edu.pe.cibertec.infracciones.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    void procesarPago_debeAplicarDescuento20_cuandoPagoEsElMismoDiaDeEmision() {
        Multa multa = new Multa();
        multa.setId(1L);
        multa.setMonto(500.00);
        multa.setFechaEmision(LocalDate.now());
        multa.setFechaVencimiento(LocalDate.now().plusDays(5));
        multa.setEstado(EstadoMulta.PENDIENTE);
        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));


        var resultado = pagoService.procesarPago(1L);


        assertEquals(400.00, resultado.getMontoPagado());
        assertEquals(EstadoMulta.PAGADA, multa.getEstado());
        verify(pagoRepository, times(1)).save(any());
    }
    @Test
    void procesarPago_debeAplicarRecargo15_cuandoMultaEstaVencida() {
        Multa multa = new Multa();
        multa.setId(2L);
        multa.setMonto(500.00);
        multa.setFechaEmision(LocalDate.now().minusDays(12));
        multa.setFechaVencimiento(LocalDate.now().minusDays(2));
        multa.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(2L)).thenReturn(Optional.of(multa));

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);


        pagoService.procesarPago(2L);


        verify(pagoRepository, times(1)).save(pagoCaptor.capture());

        Pago pagoCaptado = pagoCaptor.getValue();
        assertEquals(75.00, pagoCaptado.getRecargo());
        assertEquals(0.00, pagoCaptado.getDescuentoAplicado());
        assertEquals(575.00, pagoCaptado.getMontoPagado());
    }
}
