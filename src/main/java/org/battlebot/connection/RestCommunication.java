package org.battlebot.connection;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.battlebot.client.message.command.CheckConnectionRequest;
import org.battlebot.client.message.command.CheckConnectionResponse;
import org.battlebot.client.message.command.EnrollRequest;
import org.battlebot.client.message.command.EnrollResponse;
import org.battlebot.client.message.command.ErrorResponse;
import org.battlebot.client.message.command.FireRequest;
import org.battlebot.client.message.command.FireResponse;
import org.battlebot.client.message.command.ForwardRequest;
import org.battlebot.client.message.command.ForwardResponse;
import org.battlebot.client.message.command.RequestConnectionResponse;
import org.battlebot.client.message.command.TurnRequest;
import org.battlebot.client.message.command.TurnResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestCommunication {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestCommunication.class);
	private static ObjectMapper MAPPER = new ObjectMapper();
	protected final static CloseableHttpClient HTTPCLIENT = HttpClients.createDefault();
	private ConnectionManager cnxMgr;

	public RestCommunication(ConnectionManager cnxMgr) {
		this.cnxMgr = cnxMgr;
	}

	/**
	 * Engristrement du bot
	 * 
	 * @param teamId  identifiant de l'équipe
	 * @param botName nom du bot
	 * @return id du bot
	 * @throws Exception
	 */
	public String registerBot(String teamId, String botName) throws Exception {
		EnrollRequest req = new EnrollRequest(teamId, botName);

		HttpPost postRequest = cnxMgr.httpPost("/bots/action/register");
		HttpEntity stringEntity = new StringEntity(MAPPER.writeValueAsString(req), ContentType.APPLICATION_JSON);
		postRequest.setEntity(stringEntity);
		CloseableHttpResponse httpResponse = HTTPCLIENT.execute(postRequest);
		String responseMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		
		LOGGER.debug("bot registration response " + responseMsg);
		if(httpResponse.getStatusLine().getStatusCode() >= 400) {
			ErrorResponse response = MAPPER.readValue(responseMsg, ErrorResponse.class);
			LOGGER.error("Erreur lors de l'enrollement :" + response.getDetail().getLabel());
			throw new Exception(response.getDetail().getLabel());
		}else {
			EnrollResponse response = MAPPER.readValue(responseMsg, EnrollResponse.class);
	
			String enrollMsg = "Enrollement (" + response.getStatus() + ") : " + response.getMessage();
			if (!EnrollResponse.OK.equals(response.getStatus())) {
				throw new Exception("enrollMsg");
			} else {
				LOGGER.info(enrollMsg);
			}
			return response.getBot_id();
		}

	}

	/**
	 * Récupération de l'identifiant de validation de connecttion
	 * 
	 * @param botId identifiant du bot qui se connecte
	 * @return identifiant de session pour la validation des connections
	 * @throws Exception n
	 */
	public String getRegistrationId(String botId) throws Exception {

		HttpGet getRequest = cnxMgr.httpGet("/bots/" + botId + "/action/request_connection");
		CloseableHttpResponse httpResponse = HTTPCLIENT.execute(getRequest);
		String responseMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		RequestConnectionResponse response = MAPPER.readValue(responseMsg, RequestConnectionResponse.class);

		LOGGER.info("Identifiant de session la validation des connexions : " + response.getRequest_id());
		return response.getRequest_id();
	}

	/**
	 * Envoie des identifications de connexion pour terminer la procédure
	 * d'enregistrement du bot
	 * 
	 * @param botId     identifiant du bot
	 * @param sessionId identifiant de session d'enregistrement
	 * @param mqttId    identifiant de connexion mqtt
	 * @param stompId   identifiant de connexion stomp
	 * @return true si l'enregistrement est correct
	 */
	public boolean checkConnection(String botId, String sessionId, String mqttId, String stompId) throws Exception {
		LOGGER.info("Vérification de la connection pour bot " + botId + "(sessionId = " + sessionId + ",mqttId = "
				+ mqttId + ", stompId = " + stompId);

		CheckConnectionRequest req = new CheckConnectionRequest(sessionId, mqttId, stompId);

		HttpPatch patchRequest = cnxMgr.httpPatch("/bots/" + botId + "/action/check_connection");
		HttpEntity stringEntity = new StringEntity(MAPPER.writeValueAsString(req), ContentType.APPLICATION_JSON);
		patchRequest.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = HTTPCLIENT.execute(patchRequest);
		String responseMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		CheckConnectionResponse response = MAPPER.readValue(responseMsg, CheckConnectionResponse.class);

		boolean cnxOK = CheckConnectionResponse.OK.equals(response.getStatus());
		LOGGER.info("Connexion " + (cnxOK ? "OK" : "KO " + response.getMessage()));

		return cnxOK;
	}

	public boolean shoot(String botId, int angle) throws Exception {
		LOGGER.info("Transmission de l'ordre de tir " + angle);
		boolean ok = false;
		FireRequest req = new FireRequest(angle);

		HttpPatch patchRequest = cnxMgr.httpPatch("/bots/" + botId + "/action/shoot");
		HttpEntity stringEntity = new StringEntity(MAPPER.writeValueAsString(req), ContentType.APPLICATION_JSON);
		patchRequest.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = HTTPCLIENT.execute(patchRequest);
		String responseMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		if (httpResponse.getStatusLine().getStatusCode() >= 400) {
			ErrorResponse err =  MAPPER.readValue(responseMsg, ErrorResponse.class);
			LOGGER.error(err.toString());
		} else {
			FireResponse response = MAPPER.readValue(responseMsg, FireResponse.class);

			ok = FireResponse.OK.equals(response.getStatus());
			if (ok) {
				LOGGER.info(response.getMessage());
			} else {
				LOGGER.warn(response.getMessage());
			}
		}

		return ok;
	}

	public boolean turn(String botId, String direction) throws Exception {
		LOGGER.info("Transmission de l'ordre de tourner " + direction);
		boolean ok = false;
		TurnRequest req = new TurnRequest(direction);

		HttpPatch patchRequest = cnxMgr.httpPatch("/bots/" + botId + "/action/turn");
		HttpEntity stringEntity = new StringEntity(MAPPER.writeValueAsString(req), ContentType.APPLICATION_JSON);
		patchRequest.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = HTTPCLIENT.execute(patchRequest);
		String responseMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		if (httpResponse.getStatusLine().getStatusCode() >= 400) {
			ErrorResponse err =  MAPPER.readValue(responseMsg, ErrorResponse.class);
			LOGGER.error(err.toString());
		} else {
			TurnResponse response = MAPPER.readValue(responseMsg, TurnResponse.class);
	
			ok = TurnResponse.OK.equals(response.getStatus());
			if (ok) {
				LOGGER.info(response.getMessage());
			} else {
				LOGGER.warn(response.getMessage());
			}
		}
		return ok;
	}

	public boolean move(String botId, String action) throws Exception {
		LOGGER.info("Transmission de se déplacer " + action);
		boolean ok = false;
		ForwardRequest req = new ForwardRequest(action);

		HttpPatch patchRequest = cnxMgr.httpPatch("/bots/" + botId + "/action/move");
		HttpEntity stringEntity = new StringEntity(MAPPER.writeValueAsString(req), ContentType.APPLICATION_JSON);
		patchRequest.setEntity(stringEntity);

		CloseableHttpResponse httpResponse = HTTPCLIENT.execute(patchRequest);
		String responseMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		
		if (httpResponse.getStatusLine().getStatusCode() >= 400) {
			ErrorResponse err =  MAPPER.readValue(responseMsg, ErrorResponse.class);
			LOGGER.error(err.toString());
		} else {
			ForwardResponse response = MAPPER.readValue(responseMsg, ForwardResponse.class);
	
			ok = ForwardResponse.OK.equals(response.getStatus());
			if (ok) {
				LOGGER.info(response.getMessage());
			} else {
				LOGGER.warn(response.getMessage());
			}
		}
		return ok;
	}
}
