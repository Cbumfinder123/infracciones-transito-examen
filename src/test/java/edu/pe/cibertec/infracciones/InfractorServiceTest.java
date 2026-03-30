package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.VehiculoRepository;
import edu.pe.cibertec.infracciones.service.impl.InfractorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfractorServiceTest {

    @Mock
    private InfractorRepository infractorRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private InfractorServiceImpl infractorService;

    @Test
    void verificarBloqueo_noDebeBloquear_cuando2MultasVencidasy3Pagadas() {

        Infractor infractor = new Infractor();
        infractor.setId(1L);
        infractor.setBloqueado(false);

        when(infractorRepository.findById(1L)).thenReturn(Optional.of(infractor));
        when(infractorRepository.countByIdAndMultasEstado(1L, EstadoMulta.VENCIDA)).thenReturn(2L);


        infractorService.verificarBloqueo(1L);


        verify(infractorRepository, never()).save(any());
    }
}