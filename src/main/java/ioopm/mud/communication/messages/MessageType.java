package ioopm.mud.communication.messages;

public enum MessageType {
	GENERAL_ACTION,
	GENERAL_ERROR,
	GENERAL_REPLY,
	HEARTBEAT,
	HEARTBEAT_REPLY,
	AUTHENTICATION,
	AUTHENTICATION_REPLY,
	SERIOUS_ERROR,
	REGISTRATION,
	REGISTRATION_REPLY,
	LOGOUT,
	HANDSHAKE,
	HANDSHAKE_REPLY,
	NOTIFICATION,
	ADMIN_ACTION,
	ADMIN_REPLY,
}
