package util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.util.logging.LogLevel;
import de.uniluebeck.itm.util.logging.Logging;

public class LoggerRule implements TestRule {
	private org.slf4j.Logger logger;

	public Logger getLogger() {
		return this.logger;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				Logging.setLoggingDefaults(LogLevel.INFO, "[%-5p] %c{1}: %m%n");
				logger = LoggerFactory.getLogger(description.getTestClass());
				try {
					base.evaluate();
				} finally {
					logger = null;
				}
			}
		};
	}
}