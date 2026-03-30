package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.TipoInfraccionRepository;
import edu.pe.cibertec.infracciones.repository.VehiculoRepository;
import edu.pe.cibertec.infracciones.service.impl.MultaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultaServiceTest {

    @Mock
    private MultaRepository multaRepository;
    @Mock
    private InfractorRepository infractorRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private TipoInfraccionRepository tipoInfraccionRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    @Test
    void actualizarEstados_debeVencerMultaPendienteConFechaVencida() {

        Multa multa = new Multa();
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        when(multaRepository.findByEstado(EstadoMulta.PENDIENTE)).thenReturn(List.of(multa));


        multaService.actualizarEstados();


        verify(multaRepository, times(1)).save(multa);
        assert multa.getEstado() == EstadoMulta.VENCIDA;
    }
}