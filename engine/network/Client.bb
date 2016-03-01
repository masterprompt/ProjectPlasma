;============================
;CLIENT CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const CLIENT_MAX%=5

;Net Properties
Const NET_CLIENT_ID			=1
Const NET_CLIENT_PLAYERID		=2
Const NET_CLIENT_CONNECTIONID		=3
Const NET_CLIENT_STATE			=4
Const NET_CLIENT_TCPSTREAM		=5
Const NET_CLIENT_UDPSTREAM		=6
Const NET_CLIENT_TCPPORT		=7
Const NET_CLIENT_UDPPORT		=8
Const NET_CLIENT_LASTALIVE		=9
Const NET_CLIENT_TCPREADSTATE		=10
Const NET_CLIENT_SHORTIP		=11
Const NET_CLIENT_LONGIP			=12
Const NET_CLIENT_TIMEOUT		=13
Const NET_CLIENT_TCPWAITTIME		=14
Const NET_CLIENT_PINGTYPE		=15


;============================
;GLOBALS
;============================
Global Client_ID.Client[CLIENT_MAX%];Primary Key Object Pointer
Global Client_Index%[CLIENT_MAX%], Client_IndexPointer%;Primary Key ID Management Stack
Global Client_PingTime%

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type Client
	;Purpose: 
	;Properties:
	FIELD ID%;
	FIELD PLAYERID%;
	FIELD CONNECTIONID%;
	FIELD STATE%;
	FIELD TCPSTREAM%;
	FIELD UDPSTREAM%;
	FIELD TCPPORT%;
	FIELD UDPPORT%;
	FIELD LASTALIVE%;
	FIELD TCPREADSTATE%;
	FIELD SHORTIP%;
	FIELD LONGIP$;
	FIELD TIMEOUT%;
	FIELD TCPWAITTIME%;
	FIELD UDPWAITTIME%;
	FIELD PINGTYPE%;
	FIELD LASTPING%;
	FIELD PING%;
	Field DeleteMe%;
	FIELD UPDATERATE%;
	FIELD LASTUPDATE%;
End Type

;============================
;METHODS
;============================
Function ClientStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For Clientloop=CLIENT_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		ClientIndexPush(Clientloop)
	Next
End Function

Function ClientStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.Client=Each Client
		ClientDelete(this)
	Next
End Function

Function ClientNew.Client()
	;Purpose: CONSTRUCTOR - Creates Client Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: Client Instance
	this.Client=New Client
	THIS\ID%		=0
	THIS\PLAYERID%		=0
	THIS\CONNECTIONID%	=0
	THIS\STATE%		=0
	THIS\TCPSTREAM%		=0
	THIS\UDPSTREAM%		=0
	THIS\TCPPORT%		=0
	THIS\UDPPORT%		=0
	THIS\LASTALIVE%		=0
	THIS\TCPREADSTATE%	=0
	THIS\SHORTIP%		=0
	THIS\LONGIP$		=""
	THIS\TIMEOUT		=8000
	This\TCPWAITTIME%	=1
	This\UDPWAITTIME%	=1	
	THIS\PINGTYPE%		=0
	THIS\LASTPING%		=0
	THIS\PING%		=0
	This\DeleteMe%		=0
	THIS\UPDATERATE%	=50
	THIS\LASTUPDATE%	=0
	this\id%=ClientIndexPop()
	Client_ID[this\id%]=this
	Return this
End Function

Function ClientDelete(this.Client)
	;Purpose: DESTRUCTOR - Removes Client Instance
	;Parameters: Client Instance
	;Return: None
	If THIS\TCPSTREAM% <> 0 Then
		CloseTCPStream(THIS\TCPSTREAM%)
		THIS\TCPSTREAM%=0
	EndIf
	If THIS\UDPSTREAM% <> 0 Then
		CloseudpStream(THIS\UDPSTREAM%)
		THIS\UDPSTREAM%=0
	EndIf

	Client_ID[this\id]=Null
	ClientIndexPush(this\id%)
	Delete this
End Function

Function ClientUpdate()
	;Purpose: Main Loop Update. Updates all Client Instances.
	;Parameters: None
	;Return: None
	For this.Client=Each Client
		if THIS\TCPSTREAM% <> 0 then
			ThisWait = 0
			ThisWaitStart = Millisecs()
			ThisWaitLoop = 0
			;Loop through all data available till we cant wait any longer
			While ThisWait < this\TCPWAITTIME% and ReadAvail(THIS\TCPSTREAM%) > 0 and ThisWaitLoop < 1000
				if ReadAvail(THIS\TCPSTREAM%) >=70 then
					ThisBank.Bank = BankNew()
					THIS\LASTALIVE%=Millisecs()
					BankRecStream(ThisBank, THIS\TCPSTREAM%, 70)
					ClientProcessStream(ThisBank)
					BankDelete(ThisBank)
				Endif
				ThisWaitLoop=ThisWaitLoop+1
				ThisWait = Millisecs() -ThisWaitStart
			Wend
		endif
		if THIS\UDPSTREAM% <> 0 THEN
			;Check UDP for messages
			ThisWait = 0
			ThisWaitStart = Millisecs()
			ThisWaitLoop = 0
			;Loop through all data available till we cant wait any longer
			While ThisWait < this\UDPWAITTIME% and recvudpmsg(THIS\UDPSTREAM%) > 0 and ThisWaitLoop < 1000
				if ReadAvail(THIS\UDPSTREAM%) >=70 then
					ThisBank.Bank = BankNew()
					BankRecStream(ThisBank, THIS\UDPSTREAM%, 70)
					ClientProcessStream(ThisBank.Bank)
					BankDelete(ThisBank)
				Endif
				ThisWaitLoop=ThisWaitLoop+1
				ThisWait = Millisecs() -ThisWaitStart
			Wend
		ENDIF
		Select This\State%
			case 1;Initial connection Made
				if THIS\CONNECTIONID% <> 0 then This\State = 2
			case 2;Handshaking Complete
				;Set Connection State to request UDP Port and PlayerID
				ThisBank.Bank = ClientCreateMessage(NET_SET, NET_CONNECTION, THIS\CONNECTIONID%, 0, NET_CONNECTION_STATE)
				BankWriteByte(ThisBank,4)
				BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
				This\State = 3
			case 3;We should have our playerid and udp port, and player list
				if THIS\PLAYERID% <> 0 and THIS\UDPPORT% <> 0 then
					ThisPlayer.Player = Player_ID[This\PlayerID]
					ThisPlayer\ClientID = This\ID
					if THIS\UDPSTREAM% = 0 then THIS\UDPSTREAM% = createudpstream(THIS\UDPPORT%)
					if THIS\UDPSTREAM% <> 0 then
						This\State = 4
						Logout("Started Client UDP on Port:"+THIS\UDPPORT%)
					else
						Logout("Unable To Start Client UDP on Port:"+THIS\UDPPORT%)
						this\DeleteMe%=1
					endif
				Endif
			CASE 4
				if millisecs() - THIS\LASTPING% > 500 then;Send over a ping
					ThisBank.Bank = ClientCreateMessage(NET_SET, NET_CONNECTION, THIS\CONNECTIONID%, 0, NET_CONNECTION_PINGTYPE)
					BankWriteByte(ThisBank,1)
					BankSendStream(ThisBank, THIS\UDPSTREAM%, 70, THIS\SHORTIP%,THIS\TCPPORT%)
					THIS\LASTPING%=millisecs()
				endif
				IF THIS\PLAYERID <> 0 AND (MILLISECS()-THIS\LASTUPDATE%) >= THIS\UPDATERATE% THEN
					ThisBank.Bank = ClientCreateMessage(NET_RUN, NET_PLAYER, THIS\PLAYERID, NET_PLAYER_METHOD_UPDATEPOS,0)
					ThisPlayer.Player = Player_ID[This\PlayerID]
					BankWriteFLOAT(ThisBank,ENTITYX#(THISPLAYER\ENTITY,1))
					BankWriteFLOAT(ThisBank,ENTITYY#(THISPLAYER\ENTITY,1))
					BankWriteFLOAT(ThisBank,ENTITYZ#(THISPLAYER\ENTITY,1))
					BankSendStream(ThisBank, THIS\UDPSTREAM%, 70, THIS\SHORTIP%,THIS\TCPPORT%)
					THIS\LASTUPDATE%=millisecs()
				endif
		End select
		IF THIS\PINGTYPE% > 0 THEN
			THIS\PING% = MILLISECS()-THIS\LASTPING%
			THIS\PINGTYPE%=0
			Client_PingTime%=THIS\PING%
			THIS\LASTALIVE%=Millisecs()
		endif
		if Millisecs() -THIS\LASTALIVE% >= THIS\TIMEOUT THEN
			LogOut("Server Timed Out!  ClientID:"+THIS\ID%)
			this\DeleteMe%=1
		endif
		if this\DeleteMe% then 
			For ThisPlayer = each Player
				PlayerDelete(ThisPlayer)
			Next
			ClientDelete(This)
		endif
	Next
End Function

Function ClientWrite(this.Client,Clientfile)
	;Purpose: Writes Client Property Values to File
	;Parameters: Client Instance, Clientfile=filehandle
	;Return: None
	WriteInt(Clientfile,this\ID%)
End Function

Function ClientRead.Client(Clientfile)
	;Purpose: Reads File To Client Property Values
	;Parameters: Clientfile=filehandle
	;Return: Client Instance
	this.Client=New Client
	this\ID%=ReadInt(Clientfile)
	ClientIndexPop()
	Client_ID[this\id%]=this
	Return this
End Function

Function ClientSave(Clientfilename$="Client.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: Clientfilename$= Name of File
	;Return: None
	Clientfile=WriteFile(Clientfilename$)
	For this.Client= Each Client
		ClientWrite(this,Clientfile)
	Next
	CloseFile(Clientfile)
End Function

Function ClientOpen(Clientfilename$="Client.dat")
	;Purpose: Opens a Object Property file
	;Parameters: Clientfilename$=Name of Property File
	;Return: None
	Clientfile=ReadFile(Clientfilename$)
	Repeat
		ClientRead(Clientfile)
	Until Eof(Clientfile)
	CloseFile(Clientfile)
End Function

Function ClientCopy.Client(this.Client)
	;Purpose: Creates a Copy of a Client Instance
	;Parameters: Client Instance
	;Return: Copy of Client Instance
	copy.Client=ClientNew()
	copy\ID%=this\ID%
	Return copy
End Function

Function ClientMimic(this.Client,mimic.Client)
	;Purpose: Copies Property Values from one Client Instance To another
	;Parameters: Client Instance
	;Return: None
	mimic\ID%=this\ID%
End Function

Function ClientCreate%(LONGIP$="127.0.0.1",TCPPORT%=2700 )
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.Client=ClientNew()
	THIS\PLAYERID%		=0
	THIS\LONGIP$		=LONGIP$
	THIS\TCPPORT%		=TCPPORT%
	THIS\CONNECTIONID%	=0
	THIS\STATE%		=0
	THIS\TCPSTREAM%		=opentcpstream(THIS\LONGIP$,THIS\TCPPORT%)
	THIS\UDPSTREAM%		=0
	THIS\UDPPORT%		=0
	THIS\LASTALIVE%		=0
	THIS\TCPREADSTATE%	=0
	THIS\SHORTIP%		=0
	THIS\PINGTYPE%		=0
	if THIS\TCPSTREAM% <> 0 then 
		THIS\STATE%=1
		THIS\LASTALIVE%=millisecs()
		THIS\SHORTIP%	=TCPStreamIP(THIS\TCPSTREAM%)
		THIS\LONGIP$	=Dottedip$(THIS\SHORTIP%)
		LogOut("Stream Opened To "+THIS\LONGIP$+":"+THIS\TCPPORT%)
			;submit our client ID To Connection
			ThisBank.Bank = ClientCreateMessage(NET_SET, NET_CONNECTION, 255, 0, NET_CONNECTION_CLIENTID)
			BankWriteByte(ThisBank,This\ID)
			BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
	else
		LogOut("Stream Failed To "+THIS\LONGIP$+":"+THIS\TCPPORT%)
	endif
	Return this\id
End Function

Function ClientCreateMessage.Bank(ThisVerb, ThisObject, ThisGroup, ThisSubObject, ThisProperty)
	;Purpose: Creates a Net Message
	;Parameters: Parameters
	;Return: Bank
	;Format-Byte
	;Verb-Byte
	;Object - Byte
	;Group - Byte
	;SubObject - Byte
	;Property - Byte
	;Value: upto 64 Bytes
	;Total: 70 Bytes
	ThisBank.Bank = BankNew()
	BankWriteByte(ThisBank,55);Our Format HEader
	BankWriteByte(ThisBank,ThisVerb);Verb
	BankWriteByte(ThisBank,ThisObject);Object
	BankWriteByte(ThisBank,ThisGroup);Group
	BankWriteByte(ThisBank,ThisSubObject);SubObject
	BankWriteByte(ThisBank,ThisProperty);Property
	return ThisBank
End Function

Function ClientDestroy(Clientid%)
	;Purpose: Remove object by ID
	;Parameters: Clientid%=Object's ID
	;Return: None
	ClientDelete(Client_ID[Clientid%])
End Function

Function ClientSet()
	;Purpose: Set Client Property Values
	;Parameters: Client Instance and Properties
	;Return: None
End Function

Function ClientIndexPush(Clientid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: Clientid=Object's ID
	;Return: None
	Client_Index[Client_IndexPointer]=Clientid%
	Client_IndexPointer=Client_IndexPointer+1
End Function

Function ClientIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	Client_IndexPointer=Client_IndexPointer-1
	Return Client_Index[Client_IndexPointer]
End Function

Function ClientProcessStream(ThisBank.Bank)
	FormatByte = BankReadbyte(ThisBank)
	if FormatByte = 55 then
		ThisVerb 	= BankReadbyte(ThisBank)
		ThisObject 	= BankReadbyte(ThisBank)
		Select ThisVerb
			Case NET_SET
				Select ThisObject
					case NET_CLIENT ClientNetSet(ThisBank.Bank)
					case NET_PLAYER PlayerNetSet(ThisBank.Bank)
				End Select
			case NET_RUN
				Select ThisObject
					case NET_PLAYER PlayerNetRUN(ThisBank.Bank)
				End select
		end select
	Endif
End Function

Function ClientNetSet(ThisBank.Bank)
	ThisGroup 	= BankReadbyte(ThisBank)
	ThisSubObject 	= BankReadbyte(ThisBank)
	ThisProperty 	= BankReadbyte(ThisBank)
	This.Client = Client_ID[ThisGroup]
	Select ThisProperty
		Case NET_CLIENT_ID			this\ID%		=BankReadbyte(ThisBank)
		Case NET_CLIENT_PLAYERID		this\PLAYERID%		=BankReadbyte(ThisBank)
		Case NET_CLIENT_CONNECTIONID		this\CONNECTIONID%	=BankReadbyte(ThisBank)
		Case NET_CLIENT_STATE			this\STATE%		=BankReadbyte(ThisBank)
		Case NET_CLIENT_TCPSTREAM		this\TCPSTREAM%		=BankReadint(ThisBank)
		Case NET_CLIENT_UDPSTREAM		this\UDPSTREAM%		=BankReadint(ThisBank)
		Case NET_CLIENT_TCPPORT			this\TCPPORT%		=BankReadshort(ThisBank)
		Case NET_CLIENT_UDPPORT			this\UDPPORT%		=BankReadshort(ThisBank)
		Case NET_CLIENT_LASTALIVE		this\LASTALIVE%		=BankReadint(ThisBank)
		Case NET_CLIENT_TCPREADSTATE		this\TCPREADSTATE%	=BankReadbyte(ThisBank)
		Case NET_CLIENT_SHORTIP			this\SHORTIP%		=BankReadint(ThisBank)
		Case NET_CLIENT_LONGIP			this\LONGIP$		=BankReadstring(ThisBank)
		Case NET_CLIENT_TIMEOUT			this\TIMEOUT%		=BankReadshort(ThisBank)
		case NET_CLIENT_TCPWAITTIME		This\TCPWAITTIME%	=BankReadshort(ThisBank)
		CASE NET_CLIENT_PINGTYPE		THIS\PINGTYPE%		=BANKREADBYTE(THISBANK)
	End Select
End Function

Function ClientMethod.Client(this.Client)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
