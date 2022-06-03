// Import the functions you need from the SDKs you need
import { initializeApp } from 'firebase/app'
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCAUeaEzq7OkyNCBe1B6DZLzOoMUFmrmpc",
  authDomain: "helloworldweb-32e96.firebaseapp.com",
  projectId: "helloworldweb-32e96",
  storageBucket: "helloworldweb-32e96.appspot.com",
  messagingSenderId: "552354903030",
  appId: "1:552354903030:web:5cb3f334a51d1a9a788dd7",
  measurementId: "G-WY2EN2Z59J"
};

const vapidKey = "BFL1i855zUAnP0DOV6QJYW0H4o3KPXrH8uk14OwAWZlY-RDMtO6lni-yfCRSZSlAeGB63gsKTHE3KiYRz77JSPg"

// Initialize Firebase
const firebaseApp = initializeApp(firebaseConfig);

export { firebaseApp, vapidKey}
