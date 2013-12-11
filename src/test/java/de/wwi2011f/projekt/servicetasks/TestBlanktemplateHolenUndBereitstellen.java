package de.wwi2011f.projekt.servicetasks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Repository;
import javax.jcr.Session;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.TransientRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import de.uniluebeck.itm.util.logging.LogLevel;
import de.uniluebeck.itm.util.logging.Logging;

public class TestBlanktemplateHolenUndBereitstellen {
	private static final String REPOSITORY_CONFIG_PATH = "jackrabbit-test-conf.xml";

	private static final String REPOSITORY_DIRECTORY_PATH = "jackrabbit-test-data/";

	private static Logger log;

	private String bpmnFilename = "diagrams/BlanktemplateHolenUndBereitstellen.bpmn";

	private static Repository repository;
	private Session keepAliveSession;

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@BeforeClass
	public static void setupTest() throws Exception {
		Logging.setLoggingDefaults(LogLevel.INFO, "[%-5p] %c{1}: %m%n");
		log = LoggerFactory.getLogger(TestBlanktemplateHolenUndBereitstellen.class);

		// Set up the transient repository (this shouldn't do anything yet)...
		repository = new TransientRepository(REPOSITORY_CONFIG_PATH, REPOSITORY_DIRECTORY_PATH);

	}

	@AfterClass
	public static void afterAll() throws Exception {
		try {
			JackrabbitRepository jackrabbit = (JackrabbitRepository) repository;
			jackrabbit.shutdown();
		} finally {
			// Clean up the test data ...
		}
	}

	@Before
	public void startRepository() throws Exception {
		if (keepAliveSession == null) {
			keepAliveSession = repository.login();
		}
	}

	@After
	public void shutdownRepository() throws Exception {
		if (keepAliveSession != null) {
			try {
				keepAliveSession.logout();
			} finally {
				keepAliveSession = null;
			}
		}
	}

	@Test
	public void startProcess() throws Exception {
		String webdavTestUrl = "http://localhost:8080/repository/default";
		String testUrl = webdavTestUrl + "/test/lala.docx";

		// Start Activiti and deploy process
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();

		InputStream bpmnProcess = getClass().getClassLoader().getResourceAsStream(bpmnFilename);
		repositoryService.createDeployment().addInputStream("BlanktemplateHolenUndBereitstellen.bpmn", bpmnProcess).deploy();

		// Start instance of this process
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("webdav-berufungskommission-empfehlungsliste-template-url", webdavTestUrl);
		variableMap.put("webdav-username", "test");
		variableMap.put("webdav-passwort", "test");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process", variableMap);
		assertNotNull(processInstance.getId());

		// Test for succeess
		Sardine sardine = SardineFactory.begin("test", "test");
		assertTrue(sardine.exists(testUrl));

		log.info("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
	}
}
