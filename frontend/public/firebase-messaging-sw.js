importScripts("https://www.gstatic.com/firebasejs/8.7.1/firebase-app.js");
importScripts(
  "https://www.gstatic.com/firebasejs/8.7.1/firebase-messaging.js"
);


firebase.initializeApp({
    apiKey: "AIzaSyCAUeaEzq7OkyNCBe1B6DZLzOoMUFmrmpc",
    authDomain: "helloworldweb-32e96.firebaseapp.com",
    projectId: "helloworldweb-32e96",
    storageBucket: "helloworldweb-32e96.appspot.com",
    messagingSenderId: "552354903030",
    appId: "1:552354903030:web:5cb3f334a51d1a9a788dd7",
    measurementId: "G-WY2EN2Z59J"
  });

// Retrieve firebase messaging
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
  console.log('Received background message ', payload);

  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
  };

  self.registration.showNotification(notificationTitle,
    notificationOptions);
});
  