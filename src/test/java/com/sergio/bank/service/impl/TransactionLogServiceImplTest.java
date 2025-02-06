package com.sergio.bank.service.impl;

import com.sergio.bank.model.TransactionLog;
import com.sergio.bank.repository.TransactionLogRepository;
import com.sergio.bank.service.impl.TransactionLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Esto es necesario para que Mockito inicialice los mocks
public class TransactionLogServiceImplTest {

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private TransactionLogServiceImpl transactionLogService;

    private Pageable pageable;
    private List<TransactionLog> transactionLogs;
    private Page<TransactionLog> transactionLogPage;

    @BeforeEach
    public void setUp() {
        // Inicializamos datos de prueba
        pageable = mock(Pageable.class);
        transactionLogs = List.of(new TransactionLog(), new TransactionLog());  // Suponiendo que TransactionLog tiene un constructor vacío
        transactionLogPage = new PageImpl<>(transactionLogs);
    }

    @Test
    public void testGetAllTransactionLogs_ReturnsPage() {
        // Arrange: Simulamos que el repositorio devuelve una página de registros
        when(transactionLogRepository.findAll(pageable)).thenReturn(transactionLogPage);

        // Act: Llamamos al método de servicio
        Page<TransactionLog> result = transactionLogService.getAllTransactionLogs(pageable);

        // Assert: Verificamos que el resultado no sea nulo y que tenga el tamaño esperado
        assertNotNull(result);
        assertEquals(transactionLogs.size(), result.getContent().size());

        // Verificamos que el repositorio haya sido llamado con el pageable correcto
        verify(transactionLogRepository).findAll(pageable);
    }

    @Test
    public void testGetAllTransactionLogs_EmptyPage() {
        // Arrange: Simulamos que el repositorio devuelve una página vacía
        when(transactionLogRepository.findAll(pageable)).thenReturn(Page.empty());

        // Act: Llamamos al método de servicio
        Page<TransactionLog> result = transactionLogService.getAllTransactionLogs(pageable);

        // Assert: Verificamos que el resultado sea una página vacía
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());

        // Verificamos que el repositorio haya sido llamado
        verify(transactionLogRepository).findAll(pageable);
    }
}
