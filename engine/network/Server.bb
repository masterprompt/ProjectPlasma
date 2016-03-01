;============================
;SERVER CLASS
;Generated with TypeWriterIV
;============================

;============================
;PURPOSE
;	-Monitors TCP Server for new connections and passes them on to connection class
;	-Monitors UDP incomming data and passes them on to Connection Class
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const SERVER_MAX%=5

;============================
;GLOBALS
;============================
Global Server_ID.Server[SERVER_MAX%];Primary Key Object Pointer
Global Server_Index%[SERVER_MAX%], Server_IndexPointer%;Primary Key ID Management Stack

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type Server
	;Purpose: 
	;Properties:
	Field ID%;
	Field TCPServer%;
	Field UDPServer%;
	Field TCPPort%;
	Field UDPPort%;
	Field State%;
	FIELD UDPWAITTIME%;
End Type

;============================
;METHODS
;============================
Function ServerStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For Serverloop=SERVER_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		ServerIndexPush(Serverloop)
	Next
End Function

Function ServerStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.Server=Each Server
		ServerDelete(this)
	Next
End Function

Function ServerNew.Server(TCPPort=2700)
	;Purpose: CONSTRUCTOR - Creates Server Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: Server Instance
	this.Server=New Server
	this\ID%=0
	this\TCPPort%=TCPPort
	this\UDPPort%=TCPPort
	this\State%=0
	this\UDPWAITTIME%=1
	this\TCPServer%=CreateTCPServer(this\TCPPort%)
	this\UDPServer%=CreateUDPStream(this\UDPPort%)
	this\id%=ServerIndexPop()
	Server_ID[this\id%]=this
	If this\TCPServer% = 0 Then Runtimeerror("Error!  TCP Port ("+this\TCPPort%+") Couldn't Be Allocated!")
	If this\UDPServer% = 0 Then Runtimeerror("Error!  UDP Port ("+this\UDPPort%+") Couldn't Be Allocated!")
	LogOut("TCP Server Started On Port:"+this\TCPPort%)
	LogOut("UDP Server Started On Port:"+this\UDPPort%)
	this\State%=1
	Return this
End Function

Function ServerDelete(this.Server)
	;Purpose: DESTRUCTOR - Removes Server Instance
	;Parameters: Server Instance
	;Return: None
	If this\TCPServer% <> 0 Then CloseTCPServer(this\TCPServer%)
	If this\UDPServer% <> 0 Then CloseUDPStream(this\UDPServer%)
	LogOut("TCP Server Stopped On Port:"+this\TCPPort%)
	LogOut("UDP Server Stopped On Port:"+this\UDPPort%)
	this\State%=0
	Server_ID[this\id]=Null
	ServerIndexPush(this\id%)
	Delete this
End Function

Function ServerUpdate()
	;Purpose: Main Loop Update. Updates all Server Instances.
	;Parameters: None
	;Return: None
	local ThisWait, ThisWaitStart, ThisWaitLoop
	For this.Server=Each Server
		If this\State%=1 Then
			;Accept and assign Streams and connections
			StreamHandle = AcceptTCPStream(this\TCPServer% )
			If StreamHandle <> 0 Then;Got a stream, assign a connection
				LogOut("TCP Stream Accepted")
				ConnectionCreate%(This\ID,StreamHandle,this\UDPServer%,this\TCPPort%)
			EndIf
			;Check UDP for messages
			ThisWait = 0
			ThisWaitStart = Millisecs()
			ThisWaitLoop = 0
			;Loop through all data available till we cant wait any longer
			While ThisWait < this\UDPWAITTIME% and recvudpmsg(this\UDPServer%) > 0 and ThisWaitLoop < 1000
				if ReadAvail(this\UDPServer%) >=70 then
					ThisBank.Bank = BankNew()
					BankRecStream(ThisBank, this\UDPServer%, 70)
					ConnectionProcessStream(ThisBank.Bank)
					BankDelete(ThisBank)
				Endif
				ThisWaitLoop=ThisWaitLoop+1
				ThisWait = Millisecs() -ThisWaitStart
			Wend
		EndIf
	Next
End Function

Function ServerWrite(this.Server,Serverfile)
	;Purpose: Writes Server Property Values to File
	;Parameters: Server Instance, Serverfile=filehandle
	;Return: None
	WriteInt(Serverfile,this\ID%)
	WriteInt(Serverfile,this\TCPServer%)
	WriteInt(Serverfile,this\TCPPort%)
	WriteInt(Serverfile,this\UDPPort%)
End Function

Function ServerRead.Server(Serverfile)
	;Purpose: Reads File To Server Property Values
	;Parameters: Serverfile=filehandle
	;Return: Server Instance
	this.Server=New Server
	this\ID%=ReadInt(Serverfile)
	this\TCPServer%=ReadInt(Serverfile)
	this\TCPPort%=ReadInt(Serverfile)
	this\UDPPort%=ReadInt(Serverfile)
	ServerIndexPop()
	Server_ID[this\id%]=this
	Return this
End Function

Function ServerSave(Serverfilename$="Server.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: Serverfilename$= Name of File
	;Return: None
	Serverfile=WriteFile(Serverfilename$)
	For this.Server= Each Server
		ServerWrite(this,Serverfile)
	Next
	CloseFile(Serverfile)
End Function

Function ServerOpen(Serverfilename$="Server.dat")
	;Purpose: Opens a Object Property file
	;Parameters: Serverfilename$=Name of Property File
	;Return: None
	Serverfile=ReadFile(Serverfilename$)
	Repeat
		ServerRead(Serverfile)
	Until Eof(Serverfile)
	CloseFile(Serverfile)
End Function

Function ServerCopy.Server(this.Server)
	;Purpose: Creates a Copy of a Server Instance
	;Parameters: Server Instance
	;Return: Copy of Server Instance
	copy.Server=ServerNew()
	copy\ID%=this\ID%
	copy\TCPServer%=this\TCPServer%
	copy\TCPPort%=this\TCPPort%
	copy\UDPPort%=this\UDPPort%
	Return copy
End Function

Function ServerMimic(this.Server,mimic.Server)
	;Purpose: Copies Property Values from one Server Instance To another
	;Parameters: Server Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\TCPServer%=this\TCPServer%
	mimic\TCPPort%=this\TCPPort%
	mimic\UDPPort%=this\UDPPort%
End Function

Function ServerCreate%(ServerID%,ServerTCPServer%,ServerTCPPort%,ServerUDPPort%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.Server=ServerNew()
	this\ID%=ServerID%
	this\TCPServer%=ServerTCPServer%
	this\TCPPort%=ServerTCPPort%
	this\UDPPort%=ServerUDPPort%
	Return this\id
End Function

Function ServerDestroy(Serverid%)
	;Purpose: Remove object by ID
	;Parameters: Serverid%=Object's ID
	;Return: None
	ServerDelete(Server_ID[Serverid%])
End Function

Function ServerSet(this.Server,ServerID%,ServerTCPServer%,ServerTCPPort%,ServerUDPPort%)
	;Purpose: Set Server Property Values
	;Parameters: Server Instance and Properties
	;Return: None
	this\ID%=ServerID%
	this\TCPServer%=ServerTCPServer%
	this\TCPPort%=ServerTCPPort%
	this\UDPPort%=ServerUDPPort%
End Function

Function ServerIndexPush(Serverid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: Serverid=Object's ID
	;Return: None
	Server_Index[Server_IndexPointer]=Serverid%
	Server_IndexPointer=Server_IndexPointer+1
End Function

Function ServerIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	Server_IndexPointer=Server_IndexPointer-1
	Return Server_Index[Server_IndexPointer]
End Function

Function ServerMethod.Server(this.Server)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
