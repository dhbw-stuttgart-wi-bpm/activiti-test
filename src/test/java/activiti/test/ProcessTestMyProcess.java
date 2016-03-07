package activiti.test;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;

import util.ActivityHelper;
import util.LoggerRule;

public class ProcessTestMyProcess {
	private static final String BPMN_FILE = "diagrams/MyProcess.bpmn20.xml";

	private static final String BPMN_PROCESS_ID = "myProcess";

	@Rule
	public LoggerRule loggerRule = new LoggerRule();

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		// Get a reference to a per-test logger
		Logger log = loggerRule.getLogger();

		// Set initial variables for the process instances state
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");

		// Deploy the process and create an instance of it
		ProcessInstance processInstance = ActivityHelper.deployProcessAndCreateInstance(activitiRule, BPMN_FILE, BPMN_PROCESS_ID,
				variableMap);

		log.info("id= " + processInstance.getId() + ", process definition id = " + processInstance.getProcessDefinitionId());

		// Read a variable from the processes state
		String nameProcessVariable = (String) processInstance.getProcessVariables().get("name");
		log.info("Obtained variable 'name' from instance: " + nameProcessVariable);

	}
}