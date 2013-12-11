package de.wwi2011f.projekt.servicetasks;

import java.io.InputStream;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

public class BlanktemplateHolenUndBereitstellen implements JavaDelegate {
	private static final String WEBDAV_BERUFUNGSKOMMISSION_EMPFEHLUNGSLISTE_DOCUMENT_PROPERTY = "webdav-berufungskommission-empfehlungsliste-template-url";
	private static final String WEBDAV_USERNAME_PROPERTY = "webdav-username";
	private static final String WEBDAV_PASSWORT_PROPERTY = "webdav-passwort";

	private Logger log = LoggerFactory.getLogger(BlanktemplateHolenUndBereitstellen.class);

	private String generateDestinationUrlDocumentInstance(DelegateExecution processInstance) {
		// TODO Change to a sensible instance-based location
		return "/test/lala.docx";
	}

	@Override
	public void execute(DelegateExecution processInstance) throws Exception {
		// Load some configuration values
		String webDavPasswort = (String) processInstance.getVariable(WEBDAV_PASSWORT_PROPERTY);
		String webDavUsername = (String) processInstance.getVariable(WEBDAV_USERNAME_PROPERTY);
		String sourceUrlDocumentTemplate = (String) processInstance
				.getVariable(WEBDAV_BERUFUNGSKOMMISSION_EMPFEHLUNGSLISTE_DOCUMENT_PROPERTY);

		if(webDavPasswort==null || webDavUsername == null || sourceUrlDocumentTemplate==null)
			throw new Exception("Unable to configure WebDAV correctly");

		//WebDAV-Cient initialisieren
		Sardine sardine = SardineFactory.begin(webDavUsername, webDavPasswort);
		
		// Ziel-URL generieren
		String destinationUrlDocumentInstance = generateDestinationUrlDocumentInstance(processInstance);

		// Blankotemplate aus WebDAV (z.B. Sharepoint) laden
		log.info("Loading template document from {}", sourceUrlDocumentTemplate);
		InputStream documentTemplateInputStream = sardine.get(sourceUrlDocumentTemplate);

		// Als neues Dokument in WebDAV hochladen
		log.info("Uploading new process instance document to {}", destinationUrlDocumentInstance);
		sardine.put(destinationUrlDocumentInstance, documentTemplateInputStream);

		log.info("Done loading document template for a process instance.");
	}
}
