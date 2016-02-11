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

public class JavaScriptTaskTest {
	private static Logger log;

	private String bpmnProcessFilename = "diagrams/JavaScriptTaskDemo.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@BeforeClass
	public static void routeLoggingToSlf4j() {
		Logging.setLoggingDefaults(LogLevel.INFO, "[%-5p] %c{1}: %m%n");
		log = LoggerFactory.getLogger(JavaScriptTaskTest.class);
	}

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();

		InputStream bpmnProcess = getClass().getClassLoader().getResourceAsStream(bpmnProcessFilename);
		repositoryService.createDeployment().addInputStream("JavaScriptTaskDemo.bpmn20.xml", bpmnProcess).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("JavaScript-Task-Demo-Process", variableMap);

		String nameProcessVariable = (String) processInstance.getProcessVariables().get("MeaningOfLife");
		log.debug("Obtained variable 'MeaningOfLife' from instance: " + nameProcessVariable);

		assertNotNull(processInstance.getId());
		log.info("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
	}
}