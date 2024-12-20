# STOMP WebSocket Application

This project demonstrates real-time communication using WebSockets, STOMP, and SockJS with a Spring Boot server and a web-based frontend.

## Project Structure

### Java Server
The Spring Boot server handles WebSocket communication using STOMP protocol. The server-side code includes:

- **`CorsConfig.java`**: Configures CORS policies to allow cross-origin requests.
- **`WebSocketConfig.java`**: Configures WebSocket endpoints and message brokers.
- **`GreetingController.java`**: Handles incoming STOMP messages and broadcasts greetings.
- **`Greeting.java` and `HelloMessage.java`**: Define message structures.
- **`StompApplication.java`**: Entry point for the Spring Boot application.

### Frontend
- **`index.html`**: Contains the user interface for the WebSocket connection and message exchange.
- **`app.js`**: Contains the JavaScript logic for managing the WebSocket connection and STOMP client.

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven
- A live server to host the frontend (e.g., [Live Server extension](https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer) in VSCode)

### Instructions

1. Clone the repository and navigate to the project directory.

2. Start the Spring Boot server:
   ```bash
   mvn spring-boot:run
   ```
   By default, the server runs on port `8082`.

3. Place the `index.html` and `app.js` files in the root directory (outside the Java project). Serve them using a live server, such as:
   ```bash
   http://127.0.0.1:5500/
   ```

4. Update the `setAllowedOrigins` method in `WebSocketConfig.java` to match the origin of your live server:
   ```java
   registry.addEndpoint("/gs-guide-websocket")
           .setAllowedOrigins("http://127.0.0.1:5500") // Update this to match your live server's URL
           .withSockJS();
   ```

5. Open the live server URL in your browser and test the application.

## Detailed Explanation

### Backend

#### `WebSocketConfig.java`
- **Purpose**: Configures the WebSocket endpoint `/gs-guide-websocket`.
- **Key Points**:
  - `setAllowedOrigins(...)`: Specifies allowed origins for cross-origin requests. This must match the live server's URL.
  - `withSockJS()`: Enables SockJS fallback for browsers that do not support WebSockets.

#### `CorsConfig.java`
- **Purpose**: Configures CORS policies globally for the application.
- **Key Points**:
  - Ensures that requests from the frontend are permitted.
  - Customize `allowedOrigins` to match the frontend's URL.

#### `GreetingController.java`
- **Purpose**: Handles messages sent to the `/app/hello` destination and broadcasts responses to `/topic/greetings`.
- **Key Points**:
  - Annotated with `@MessageMapping` and `@SendTo`.
  - Simulates processing delays using `Thread.sleep()`.

### Frontend

#### `app.js`
- **Purpose**: Manages the WebSocket connection and STOMP client.
- **Key Functions**:
  - `connect()`: Activates the STOMP client.
  - `disconnect()`: Deactivates the STOMP client.
  - `sendName()`: Publishes a message to the backend.
  - `showGreeting()`: Displays received messages in the UI.

#### `index.html`
- **Purpose**: Provides a simple UI for the application.
- **Key Elements**:
  - **Connect**: Establishes a WebSocket connection.
  - **Disconnect**: Closes the WebSocket connection.
  - **Send**: Sends the user's name to the backend.
  - **Table**: Displays greetings received from the backend.

### How SockJS, STOMP, and the Connection Work

#### SockJS
- **Purpose**: Provides a WebSocket-like object for browsers that may not support WebSockets.
- **Key Features**:
  - Attempts to establish a native WebSocket connection first.
  - Falls back to alternative transport methods like XHR-streaming or XHR-polling if WebSockets are not supported.
  - Simplifies the handling of transport-level differences between browsers.
- **In the Code**:
  ```javascript
  const socket = new SockJS('http://localhost:8082/gs-guide-websocket');
  ```
  - This creates a SockJS object that acts as the transport layer for the WebSocket connection.

#### STOMP (Simple/Streaming Text Oriented Messaging Protocol)
- **Purpose**: A protocol for messaging over WebSockets, providing publish-subscribe and point-to-point communication.
- **Key Features**:
  - Defines how messages are sent, received, and acknowledged.
  - Allows clients to subscribe to destinations (topics) and send messages to specific destinations.
- **In the Code**:
  ```javascript
  const stompClient = new StompJs.Client({
      webSocketFactory: () => socket,
  });
  ```
  - `StompJs.Client` uses the SockJS object to establish a STOMP session.
  - Handles subscribing to topics and sending messages.

#### Connection Workflow
1. **Connection Initialization**:
   - A SockJS instance is created and passed to the STOMP client.
   - The client activates the connection by calling `stompClient.activate()`.
2. **On Connect**:
   - The `onConnect` callback is triggered when the connection is established.
   - Subscriptions to topics like `/topic/greetings` are created.
3. **Message Handling**:
   - When a message is sent to `/app/hello`, the backend processes it and broadcasts a response to `/topic/greetings`.
   - The client receives this response and executes the callback function to display the message.

#### Key Methods
- **`stompClient.activate()`**: Starts the connection.
- **`stompClient.deactivate()`**: Closes the connection.
- **`stompClient.subscribe(destination, callback)`**: Subscribes to a destination (e.g., `/topic/greetings`) and executes the callback on message receipt.
- **`stompClient.publish({ destination, body })`**: Sends a message to a specified destination.

### Workflow

1. **Connect**: The user clicks "Connect" to establish a WebSocket connection.
2. **Send**: The user enters their name and clicks "Send". The frontend sends a message to `/app/hello`.
3. **Process**: The backend processes the message and sends a response to `/topic/greetings`.
4. **Display**: The frontend receives the response and displays it in the UI.

## Notes

- The `allowedOrigins` setting in `WebSocketConfig.java` must be updated to match the origin of the live server.
- Use `http://127.0.0.1:5500` during development if using the Live Server extension.

## Troubleshooting

- **WebSocket Connection Fails**: Ensure the `setAllowedOrigins` configuration matches the frontend's URL.
- **CORS Errors**: Verify the `CorsConfig` settings.
- **Frontend Not Loading**: Check the live server URL and ensure the files are served correctly.

## License
This project is licensed under the MIT License.
