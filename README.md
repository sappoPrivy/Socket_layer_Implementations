# Socket layer implementations
_IK1203 Networks and Communication, 7.5 credits at KTH Royal Institute of Technology_, Spring 2025  
Republished code developed by Tenzin Sangpo Choedon  
Skeleton declaration provided by KTH  

## 📄 Overview
This project implements networking applications through the socket API. This entails designing the client and server side of the client-server communication architercture. The client side is a general-purpose TCP client that can communicate with servers through different application protocols. The web server in this project processes the http requests through calling TCPClient.askServer(). This HTTPAsk server is also concurrent which implies it can process the request of multiple clients in parallel. The HTTP request can be made in the web broswer and the resulting HTTP response is displayed in the web browser.

## 🗂️ Table of Contents

- [Overview](#-overview)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Usage](#-usage)

## 🏗️ Project Structure

root/  
├── tcpclient/  
│   └── TCPClient.java  
├── ConcHTTPAsk.java  
├── MyRunnable.java  
├── README.md  
└── .gitignore  

## ✅ Prerequisites

**Java 18**  
- Required Java version:  
  ```bash
  sudo apt install openjdk-18-jdk
  ```

  **curl**  
- Install curl using:  
  ```bash
  sudo apt install curl
  ```

## 🚀 Usage

Run the scripts in this order:

1. **Client connects with a TCP server through terminal**  
   ```bash
   java TCPAsk time.nist.gov 13
   ```
1. **Run the HTTPAsk server**  
   ```bash
   java ConcHTTPAsk 8888
   ```
2. **Open TCP connection through web-based approach and send GET request**  
   ```bash
   curl http://hostname.domain/ask?hostname=time.nist.gov&limit=1200&port=13
   ```

