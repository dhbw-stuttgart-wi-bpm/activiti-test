package de.wwi2011f.projekt.servicetasks;

import java.util.Random;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.wwi2011f.projekt.tools.ServiceTools;

public class JavaTest2 implements JavaDelegate {

	Logger log = LoggerFactory.getLogger(JavaTest1.class);

	@Override
	public void execute(DelegateExecution processInstance) throws Exception {
		// Dump state at the beginning
		log.info("------------------------------------------------------------");
		log.info("Java Service Task {} called", this.getClass().getName());
		log.info("------------------------------------------------------------");
		ServiceTools.dumpInstanceState(log, processInstance);

		// Do something
		log.info("Adding variable to the instance state");
		processInstance.setVariable("state-task-" + this.getClass().getSimpleName(), "state-" + new Random().nextInt(100));

		// Dump state at the end
		log.info("Variables before end");
		ServiceTools.dumpVariables(log, processInstance);
	}

}
