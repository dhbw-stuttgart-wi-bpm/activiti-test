package activiti.test;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.util.logging.LogLevel;
import de.uniluebeck.itm.util.logging.Logging;

public class BookOrderProcessTest {
	private static Logger log;

	private static final String BPMN_FILE = "diagrams/BookOrderProcess.bpmn20.xml";

	private static final String BPMN_PROCESS_ID = "BookOrderProcess";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@BeforeClass
	public static void routeLoggingToSlf4j() {
		Logging.setLoggingDefaults(LogLevel.INFO, "[%-5p] %c{1}: %m%n");
		log = LoggerFactory.getLogger(BookOrderProcessTest.class);
	}

	@Test
	public void startProcess() throws Exception {

		// Obtain a reference to Activiti's runtime service
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();

		// Read the BPMN 2.0 file from the filesystem and deploy the process to Activiti
		InputStream bpmnProcessInputStream = getClass().getClassLoader().getResourceAsStream(BPMN_FILE);
		repositoryService.createDeployment().addInputStream(BPMN_FILE, bpmnProcessInputStream).deploy();

		// Set initial variables for the process instances state
		Map<String, Object> initialProcessVariables = new HashMap<String, Object>();
		initialProcessVariables.put("book_isbn", "1234");

		// Create an instance of the process and verify that is has started
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(BPMN_PROCESS_ID, initialProcessVariables);
		assertNotNull(processInstance.getId());

		log.info("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
	}
}