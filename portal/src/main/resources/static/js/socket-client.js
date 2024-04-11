'use strict';

// ////////////////////////////////////////////////////////////////////////////
// Configuration
// ////////////////////////////////////////////////////////////////////////////
const APP_PATH = '/portal';
const SOCKET_PATH = '/portal-ws';

const RECONNECT_DELAY_MILLI_SECONDS = 2000;
const MAX_RECONNECT_COUNT = 10;
const HEARTBEAT_INTERVAL_MILLI_SECONDS = 5000;

const SUBSCRIBE_DELAY_MILLI_SECONDS = 1000;
const SEND_JOIN_DELAY_MILLI_SECONDS = 1000;
// const MAX_RECONNECT_DELAY_MILLISECONDS = 1000 * 60;

let socketClient = null;

/**
 * SocketClient Class
 */
class SocketClient {

	#sockjs = null;
	#stompClient = null;
	#reconnectCount = 0;
	#headers = null;
	#connectCallback = null;
	#errorCallback = null;

	/**
	 * Constructor
	 *
	 * @param headers {Object} 연결시(STOMP) 헤더
	 * @param connectCallback {Function} 연결 성공시 콜백
	 * @param errorCallback {Function} 연결 오류시 콜백
	 *
	 * @constructor
	 */
	constructor(headers, connectCallback, errorCallback) {

		if (socketClient)
			return socketClient;

		this.#headers = headers ? headers : {};
		this.#connectCallback = connectCallback ? connectCallback : this.defaultConnectCallback;
		this.#errorCallback = errorCallback ? errorCallback : this.defaultErrorCallback;

		socketClient = this;
	}

	/**
	 * Connect to STOMP Endpoint
	 */
	connect() {

		this.#sockjs = new SockJS(APP_PATH + SOCKET_PATH);
		this.#stompClient = Stomp.over(this.#sockjs);
		this.#stompClient.reconnect_delay = RECONNECT_DELAY_MILLI_SECONDS;
		this.#stompClient.heartbeat.outgoing = HEARTBEAT_INTERVAL_MILLI_SECONDS;
		this.#stompClient.heartbeat.incoming = HEARTBEAT_INTERVAL_MILLI_SECONDS;
		// this.stompClient.debug = function() {};

		this.#stompClient.connect(this.#headers, this.#connectCallback, this.#errorCallback);
	}

	/**
	 * Default Callback, STOMP client 연결 성공시
	 *
	 * @param frame {Object} StompFrame
	 */
	defaultConnectCallback(frame) {

		console.info('STOMP CONNECTED, FRAME: %O', frame);
	}

	/**
	 * Default Callback, STOMP client 연결 오류시
	 *
	 * @param error {Object} StompFrame or StompMessage
	 */
	defaultErrorCallback(error) {

		console.info('STOMP ERROR, ERROR: %O', error);
	}

	/**
	 * Get STOMP Client
	 * @returns {Object} STOMP Client
	 */
	getStompClient() {

		return this.#stompClient;
	}

	/**
	 * Get Status of STOMP connection
	 *
	 * @returns {Boolean}
	 */
	isConnected() {

		if (this.#stompClient !== null) {
			return this.#stompClient.connected;
		}

		return false;
	}
}
