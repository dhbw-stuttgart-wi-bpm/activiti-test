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

public class SubProcessDemo {

	private static final String BPMN_FILE = "diagrams/SubProcessDemo.bpmn20.xml";

	private static final String BPMN_PROCESS_ID = "SubProcessDemo";

	@Rule
	public LoggerRule loggerRule = new LoggerRule();

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		// Get a reference to a per-test logger
		Logger log = loggerRule.getLogger();

		// Set initial variables for the process instances state
		Map<String, Object> initialProcessVariables = new HashMap<String, Object>();

		// Deploy the process and create an instance of it
		ProcessInstance processInstance = ActivityHelper.deployProcessAndCreateInstance(activitiRule, BPMN_FILE, BPMN_PROCESS_ID,
				initialProcessVariables);

		log.info("id= " + processInstance.getId() + ", process definition id = " + processInstance.getProcessDefinitionId());
	}
}