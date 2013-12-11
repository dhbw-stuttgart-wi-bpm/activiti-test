package org.activiti.designer.test;

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

public class ProcessTestMyProcess {
	private static Logger log;

	private String filename = "diagrams/MyProcess.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@BeforeClass
	public static void routeLoggingToSlf4j() {
		Logging.setLoggingDefaults(LogLevel.INFO, "[%-5p] %c{1}: %m%n");
		log = LoggerFactory.getLogger(ProcessTestMyProcess.class);
	}

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();

		InputStream bpmnProcess = getClass().getClassLoader().getResourceAsStream(filename);
		repositoryService.createDeployment().addInputStream("myProcess.bpmn20.xml", bpmnProcess).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variableMap);

		// String object =
		// (String)processInstance.getProcessVariables().get("name");

		assertNotNull(processInstance.getId());
		log.info("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
	}
}