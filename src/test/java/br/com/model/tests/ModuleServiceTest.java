package br.com.model.tests;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModuleServiceTest {

	//@MockBean
	//ModuleRepository moduleRepository;
	/*
	@Test
	public void emptyTest () {
		System.out.println(Translator.toLocale("model.hello"));
		assertTrue(true);
	}

	@Autowired
	ModuleService moduleService;
	
	@Test
	public void testSaveSucess() {
		when(moduleRepository.findByDescription(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		when(moduleRepository.save(mockModule())).thenReturn(null);
		moduleService.save(mockModule());
		assertTrue(true);
	}

	@Test
	public void testSaveAllreadyExists() {
		when(moduleRepository.findByDescription(Mockito.anyString())).thenReturn(Optional.ofNullable(mockModule()));
		when(moduleRepository.save(mockModule())).thenReturn(null);
		assertThrows(BusinessRunTimeException.class, () -> moduleService.save(mockModule()));
	}
	
	@Test
	public void delete () {
		doNothing().when(moduleRepository).deleteById(1);
		moduleService.delete(1);
		assertTrue(true);
	}
	
	public Module mockModule () {
		Module module = new Module();
		module.setDescription("test");
		module.setAvailable(true);
		module.setId(1);
		return module;		
	}*/
}
