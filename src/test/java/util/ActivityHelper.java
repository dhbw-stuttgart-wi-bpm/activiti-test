package util;

import java.io.InputStream;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;

public class ActivityHelper {

	public static ProcessInstance deployProcessAndCreateInstance(ActivitiRule activiti, String processResourceLocation, String processId,
			Map<String, Object> initialProcessInstanceVariables) throws Exception {

		// Obtain a reference to Activiti's runtime service
		RepositoryService repositoryService = activiti.getRepositoryService();
		RuntimeService runtimeService = activiti.getRuntimeService();

		// Read the BPMN 2.0 file from the filesystem and deploy the process to Activiti
		InputStream bpmnProcess = activiti.getClass().getClassLoader().getResourceAsStream(processResourceLocation);
		repositoryService.createDeployment().addInputStream(processResourceLocation, bpmnProcess).deploy();

		// Create an instance of the process and verify that is has started
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId, initialProcessInstanceVariables);

		if (processInstance.getProcessDefinitionId() == null)
			throw new Exception("Process could not be started");

		return processInstance;
	}

}