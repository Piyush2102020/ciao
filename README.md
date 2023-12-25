<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">

</head>
<body>

  <h1>Ciao Chatroom Application</h1>

  <p>Ciao is a chatroom application written in Java and XML that enables users to engage in chat rooms and exchange personal messages. The application utilizes Firebase for real-time messaging functionalities.</p>

  <h2>Features</h2>

  <ul>
    <li><strong>Chatroom Functionality:</strong> Users can join various chat rooms, exchange messages and pictures, and interact with others.</li>
    <li><strong>Personal Messages:</strong> Allows sending private messages to individual users.</li>
    <li><strong>Firebase Integration:</strong> Utilizes Firebase for real-time messaging features.</li>
  </ul>

  <h2>Getting Started</h2>

  <h3>Prerequisites</h3>

  <p>Before running the application, make sure you have:</p>

  <ul>
    <li>Firebase service account JSON file (<code>serviceAccountKey.json</code>) for application authentication.</li>
    <li>Firebase Realtime Database URL to connect your app to Firebase.</li>
  </ul>

  <h3>Installation</h3>

  <ol>
   Clone the repository:
    git clone https://github.com/your-username/ciao-chatroom.git
cd ciao-chatroom
  

Firebase Configuration:
    Place your <code>serviceAccountKey.json</code> file inside the app directory. Replace <code>YOUR_DATABASE_URL</code> in the Firebase initialization code with your Firebase Realtime Database URL.

   Run the Application:
    <p>Open the project in Android Studio and run the application.</p>
  </ol>

  <h2>Firebase Configuration</h2>

  <p>To configure Firebase for this application:</p>

  <ol>
    <li>Create a Firebase project on the <a href="https://console.firebase.google.com/">Firebase Console</a>.</li>
    <li>Generate a service account JSON file (<code>serviceAccountKey.json</code>) from the Firebase project settings.</li>
    <li>Set up Realtime Database in Firebase and obtain the Database URL.</li>
    <li>Add the <code>serviceAccountKey.json</code> file and replace the Firebase Database URL in the application code.</li>
  </ol>

  <h2>Technologies Used</h2>

  <ul>
    <li>Java</li>
    <li>XML</li>
    <li>Firebase Realtime Database</li>
  </ul>

  <h2>Contributing</h2>

  <p>Contributions are welcome! Feel free to submit pull requests or open issues.</p>


  <h2>Acknowledgments</h2>

  <p>Thanks to Firebase for providing a robust backend infrastructure for real-time messaging.</p>

</body>
</html>
