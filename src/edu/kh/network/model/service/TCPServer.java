package edu.kh.network.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
// Exception 될만한 애들이 너무 많이 들어와있음!! 그래서 예외처리 먼저 해주자

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	// Server : 서비스를 제공하는 프로그램 또는 컴퓨터
	// Client : 서버에 서비스를 요청하여 사용하는 프로그램 또는 컴퓨터
	
	// TCP Socket 프로그래밍
	// TCP : 데이터 전달의 신뢰성을 최대한 보장하기 위한 방식 (연결 지향형 통신)
	//          순차적으로 데이터를 전달하고 확인 및 오류 시 재전송
	
	// Socket : 프로세스의 통신에 사용되는 양 끝단.
	//             서버와 클라이언트가 통신을 하기 위한 매개체
	
	// ServerSocket : 서버용 소켓
	//   - Port와 연결되어 외부 요청을 기다리는 역할
	//   -> 클라이언트 요청이 올 경우 이를 수락(accept)하고 클라이언트가 사용할 수 있는 소켓을 생성
	//   ---> 서버 소켓과 클라이언트 소켓이 연결되어 데이터 통신이 가능해짐
	
	
	public void serverStart() {
		// 1. 서버의 포트번호 정함
		// 0~65535 까지 지정 가능함 근데 1023이하는 이미 사용하는 경우가 많아서 그 이상으로 쓰는게 좋음
		int port =  8500;
		
		/* 사용할 변수를 미리 선언 */
		ServerSocket serverSocket = null; // 서버 소켓 저장용 변수
		Socket clientSocket = null; // 클라이언트 소켓 저장 변수
		
		InputStream is = null; // 클라이언트 -> 서버 입력용 스트림 변수
		BufferedReader br = null; // 입력용 보조 스트림 변수 입력이 되면 그걸 읽는걸 도와줌
		
		OutputStream os = null;	// 서버에서 ->클라이언트로 출력하는 스트림 변수도 필요함 그래서 이거 씀
		PrintWriter pw = null; // 출력용 보조 스트림 젼수
		//BufferedWriter br = null;
		// 위에 두개가 무슨 차이냐면 프린트는 출력함수를 제공하줌(println 등)으로써 파일 출력을 편하게 해준다
		//버퍼는 위에 버퍼리더같은것 걍 입맛에 맞게 쓰면 됨
		
		
		
	
		
		
		

		try {
			// 2. 서버용 소켓 객체 생성
			serverSocket = new ServerSocket(port); // 서버용 소켓을 생성하여 포트 결합
			
			// 3. 클라이언트 쪽에서 접속 요청이 오길 기다림(요청이 올때까지 진행되지 않음
			// 서버용 소켓은 생성되면 클라이언트 요청이 오기 전까지 다음 코드를 수행하지 않고 대기하고 있음
			
			System.out.println("[Server]");
			System.out.println("클라이언트 요청을 기다리고 있습니다...");
			
			// 4. 접속 요청이 오면 요청 수락 후 해당 클라이언트에 대한 소켓 객체 생성
			clientSocket = serverSocket.accept(); // 요청 수락한것에 대해 클라이언트 소켓에 저장됨
			// 요청을 수락하면 자동으로 Socket객체가 얻어와진다
			// 하고 안하고의 차이? => 빨간 불 들어옴(코드 다시 돌리려면 빨간거 꼭 끄고 하기)
			
			String clientIP = clientSocket.getInetAddress().getHostAddress();  // IP주소 형태 만들기
			// 접속한 클라이언트의 IP를 얻어와 출력
			
			System.out.println(clientIP + "가 연결을 요청함..."); 
			
			// 5. 연결된 클라이언트와 입출력 스트림 생성
			is = clientSocket.getInputStream(); // clientSocket에서 제공하는 스트림
			os = clientSocket.getOutputStream(); // clientSocket에서 제공하는 스트림
			
			// 6. 보조 스트림을 통해 성능 개선
			br = new BufferedReader(new InputStreamReader(is));  // ()에 is를 쳐버리면 성능 개선 해주려는걸 무시하는거임
															// InputStreamReader 안에 is 써 주어야 함
															//	InputStreamReader : 바이트기반 스트림과 문자기반 스트림 연결에 사용하는 스트림
			
			pw = new PrintWriter(os);  // 얘는 걍 os 넣으면 됨
			
			// 7. 스트림을 통해 읽고 쓰기
			pw.println("[서버 접속 성공]");
			pw.flush(); // 스트림에 있는 내용을 모두 밀어내는 역할 // 스트림을 닫지않고 밀어냄
			// close()도 밀어내는게 가능한데, 굳이 flush()를 사용하는 까닭
			// 클로즈는 연결을 끊고 내용을 밀어낼 때 사용하는 것
			// ==> 스트림을 닫지 않은 샅애에서 내용을 내보내고 싶은 경우가 있음
			
			// 7-2 ) 클라이언트 -> 서버에게 입력(메세지 전송받기)
			String message = br.readLine();// 클라이언트 메세지를 한 줄 읽어옴
			System.out.println(clientIP + "가 보낸 메세지 : " + message);
			
			
			
		} catch(IOException e){  // 임포트를 해도 오류가 날텐데 이유는 아직 예외처리 할 애들을 적지 않아서
			e.printStackTrace(); //예외 추적
		
		} finally {
			// 8. 통신 종료
			// 사용한 스트림, 소켓 자원을 모두반환( close)
			try {
				// 보조스트림 close시 연결된 기반스트림 (is, os)도 같이 close됨
				if(pw != null) pw.close();
				if(br != null)br.close();
				if(serverSocket != null)serverSocket.close();
				if(clientSocket != null)clientSocket.close();
			
			} catch(IOException e) {
				e.printStackTrace();
			}
		
			// 위에서 쓴거 다 닫아주셈
			// 밑에 세개는IOException이 일어날 수 있어서 try catch로 잡아줘야 함
			
	
		}
		

		
		
		
		
		
	}

}
