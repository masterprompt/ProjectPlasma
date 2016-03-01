;============================
;CONNECTION CLASS
;Generated with TypeWriterIV
;============================

;============================
;PURPOSE
;	-Monitors TCP Incomming Data
;	-Processes TCP incomming Data
;	-Processes UDP incomming Data
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const CONNECTION_MAX%=500
;Net Properties
Const NET_CONNECTION_ID			=1
Const NET_CONNECTION_STATE		=2
Const NET_CONNECTION_SERVERID		=3
Const NET_CONNECTION_PLAYERID		=4
Const NET_CONNECTION_NAME		=5
Const NET_CONNECTION_PING		=6
Const NET_CONNECTION_LASTALIVE		=7
Const NET_CONNECTION_TCPSTREAM		=8
Const NET_CONNECTION_UPDSTREAM		=9
Const NET_CONNECTION_TCPPORT		=10
Const NET_CONNECTION_UDPPORT		=11
Const NET_CONNECTION_TCPREADSTATE	=12
Const NET_CONNECTION_IPSHORT		=13
Const NET_CONNECTION_IPLONG		=14
Const NET_CONNECTION_TCPWAITTIME	=15
Const NET_CONNECTION_TIMEOUT		=16
CONST NET_CONNECTION_CLIENTID		=17
CONST NET_CONNECTION_PINGTYPE		=18
;============================
;GLOBALS
;============================
Global Connection_ID.Connection[CONNECTION_MAX%];Primary Key Object Pointer
Global Connection_Index%[CONNECTION_MAX%], Connection_IndexPointer%;Primary Key ID Management Stack

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type Connection
	;Purpose: 
	;Properties:
	FIELD ID%;
	FIELD STATE%;
	FIELD SERVERID%;
	FIELD PLAYERID%;
	FIELD CLIENTID%;
	FIELD NAME$;
	FIELD PING%;
	FIELD LASTALIVE%;
	FIELD TCPSTREAM%;
	FIELD UPDSTREAM%;
	FIELD TCPPORT%;
	FIELD UDPPORT%;
	FIELD TCPREADSTATE%;
	FIELD IPSHORT%;
	FIELD IPLONG$;
	FIELD TCPWAITTIME%;
	FIELD TIMEOUT%;
	FIELD PINGTYPE%;
	FIELD UPDATERATE%;
	FIELD LASTUPDATE%;
	FIELD DELETEME%;
End Type

;============================
;METHODS
;============================
Function ConnectionStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For Connectionloop=CONNECTION_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		ConnectionIndexPush(Connectionloop)
	Next
End Function

Function ConnectionStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.Connection=Each Connection
		ConnectionDelete(this)
	Next
End Function

Function ConnectionNew.Connection()
	;Purpose: CONSTRUCTOR - Creates Connection Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: Connection Instance
	this.Connection=New Connection
	this\ID%		=0
	this\STATE%		=0
	this\SERVERID%		=0
	this\PLAYERID%		=0
	this\NAME$		=""
	this\PING%		=0
	this\LASTALIVE%		=0
	this\TCPSTREAM%		=0
	this\UPDSTREAM%		=0
	this\TCPPORT%		=0
	this\UDPPORT%		=0
	this\TCPREADSTATE%	=0
	this\IPSHORT%		=0
	this\IPLONG$		=""
	this\TCPWAITTIME%	=1
	THIS\CLIENTID%		=0
	THIS\TIMEOUT		=8000
	THIS\PINGTYPE%		=0
	THIS\UPDATERATE%	=50
	THIS\LASTUPDATE%	=0
	this\id%=ConnectionIndexPop()
	Connection_ID[this\id%]=this
	Return this
End Function

Function ConnectionDelete(this.Connection)
	;Purpose: DESTRUCTOR - Removes Connection Instance
	;Parameters: Connection Instance
	;Return: None
	IF this\TCPSTREAM% <> 0 THEN CLOSETCPSTREAM(this\TCPSTREAM%)
	Connection_ID[this\id]=Null
	ConnectionIndexPush(this\id%)
	this\STATE		=0
	this\NAME$		=""
	Delete this
End Function

Function ConnectionUpdate()
	;Purpose: Main Loop Update. Updates all Connection Instances.
	;Parameters: None
	;Return: None
	For this.Connection=Each Connection
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
					if This\ClientID = 0 then
						ConnectionProcessStreamHandshake(This.Connection, ThisBank.Bank)
					else
						ConnectionProcessStream(ThisBank.Bank)
					endif
					BankDelete(ThisBank)
				Endif
				ThisWaitLoop=ThisWaitLoop+1
				ThisWait = Millisecs() -ThisWaitStart
			Wend
			Select This\State%
				case 2;Send him a UDP port, and ConnectionID
					ThisBank.Bank = ConnectionCreateMessage(NET_SET, NET_CLIENT, this\ClientID, 0, NET_CLIENT_CONNECTIONID)
					BankWritebyte(ThisBank,this\ID%)
					BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
					This\State%=3
				case 3;Handshaking Complete

				case 4;Send him a UDP Port and a PlayerID
					this\UDPPORT%=ConnectionGetUDP(this\IPSHORT% , this\TCPPORT%)
					ThisBank.Bank = ConnectionCreateMessage(NET_SET, NET_CLIENT, this\ClientID, 0, NET_CLIENT_UDPPORT)
					BankWriteShort(ThisBank,this\UDPPORT%)
					BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
					ThisPlayer.Player =PlayerNew()
					This\PlayerID = ThisPlayer\ID
					;Send player list
					For Copy.Connection = each Connection
						if Copy\PlayerID <> 0 then
							;Send this copy player to our new player
							ThisBank.Bank = ConnectionCreateMessage(NET_RUN, NET_PLAYER,  0, NET_PLAYER_METHOD_CREATE,0)
							BankWriteBYTE(ThisBank,copy\PlayerID)
							BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
							;Send us to the copy player
							ThisBank.Bank = ConnectionCreateMessage(NET_RUN, NET_PLAYER,  0, NET_PLAYER_METHOD_CREATE,0)
							BankWriteBYTE(ThisBank,this\PlayerID)
							BankSendStream(ThisBank, Copy\TCPSTREAM%, 70)
						endif
					Next
					;Send his player id
					ThisBank.Bank = ConnectionCreateMessage(NET_SET, NET_CLIENT, this\ClientID, 0, NET_CLIENT_PLAYERID)
					BankWriteBYTE(ThisBank,This\PlayerID)
					BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
					;Initiate Control map on player
					ThisBank.Bank = ConnectionCreateMessage(NET_SET, NET_PLAYER, this\playerid, 0, NET_PLAYER_CONTROLMAPID)
					BankWriteBYTE(ThisBank,1)
					BankSendStream(ThisBank, THIS\TCPSTREAM%, 70)
					This\State%=5
				case 5
					IF THIS\PLAYERID <> 0 AND (MILLISECS()-THIS\LASTUPDATE%) >= THIS\UPDATERATE% THEN
						for ThisPlayer.player = each player
							ThisBank.Bank = ClientCreateMessage(NET_RUN, NET_PLAYER, THISPLAYER\ID, NET_PLAYER_METHOD_UPDATEPOS, 0)
							;ThisPlayer.Player = Player_ID[This\PlayerID]
							BankWriteFLOAT(ThisBank,ENTITYX#(THISPLAYER\ENTITY,1))
							BankWriteFLOAT(ThisBank,ENTITYY#(THISPLAYER\ENTITY,1))
							BankWriteFLOAT(ThisBank,ENTITYZ#(THISPLAYER\ENTITY,1))
							BankSendStream(ThisBank, THIS\UPDSTREAM%, 70, THIS\IPSHORT%,THIS\UDPPORT%)
						next
						THIS\LASTUPDATE%=millisecs()
					endif

			End select
			IF THIS\PINGTYPE% > 0 THEN
				ThisBank.Bank = ConnectionCreateMessage(NET_SET, NET_CLIENT, this\ClientID, 0, NET_CLIENT_PINGTYPE)
				BankWriteBYTE(ThisBank,1)
				BankSendStream(ThisBank, this\UPDSTREAM%, 70, THIS\IPSHORT%, THIS\UDPPORT%)
				THIS\PINGTYPE%=0
				THIS\LASTALIVE%=Millisecs()
			endif
		endif
		if Millisecs() -THIS\LASTALIVE% >= THIS\TIMEOUT THEN
			LogOut("Client Timed Out!  ConnectionID:"+THIS\ID%)
			THIS\DELETEME%=1
		endif
		IF THIS\DELETEME% THEN 
			ThisPlayerID = This\PlayerID
			ConnectionDelete(This)
			For Copy.Connection = each Connection
				if Copy\PlayerID <> 0 AND ThisPlayerID <> 0 then
					;Send this copy player to our new player
					ThisBank.Bank = ConnectionCreateMessage(NET_RUN, NET_PLAYER,  0, NET_PLAYER_METHOD_DESTROY,0)
					BankWriteBYTE(ThisBank,ThisPlayerID)
					BankSendStream(ThisBank, COPY\TCPSTREAM%, 70)
				endif
			Next
		ENDIF
	Next
End Function

Function ConnectionWrite(this.Connection,Connectionfile)
	;Purpose: Writes Connection Property Values to File
	;Parameters: Connection Instance, Connectionfile=filehandle
	;Return: None
	WriteInt(Connectionfile,this\ID%)
End Function

Function ConnectionRead.Connection(Connectionfile)
	;Purpose: Reads File To Connection Property Values
	;Parameters: Connectionfile=filehandle
	;Return: Connection Instance
	this.Connection=New Connection
	this\ID%=ReadInt(Connectionfile)
	ConnectionIndexPop()
	Connection_ID[this\id%]=this
	Return this
End Function

Function ConnectionSave(Connectionfilename$="Connection.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: Connectionfilename$= Name of File
	;Return: None
	Connectionfile=WriteFile(Connectionfilename$)
	For this.Connection= Each Connection
		ConnectionWrite(this,Connectionfile)
	Next
	CloseFile(Connectionfile)
End Function

Function ConnectionOpen(Connectionfilename$="Connection.dat")
	;Purpose: Opens a Object Property file
	;Parameters: Connectionfilename$=Name of Property File
	;Return: None
	Connectionfile=ReadFile(Connectionfilename$)
	Repeat
		ConnectionRead(Connectionfile)
	Until Eof(Connectionfile)
	CloseFile(Connectionfile)
End Function

Function ConnectionCopy.Connection(this.Connection)
	;Purpose: Creates a Copy of a Connection Instance
	;Parameters: Connection Instance
	;Return: Copy of Connection Instance
	copy.Connection=ConnectionNew()
	copy\ID%=this\ID%
	Return copy
End Function

Function ConnectionMimic(this.Connection,mimic.Connection)
	;Purpose: Copies Property Values from one Connection Instance To another
	;Parameters: Connection Instance
	;Return: None
	mimic\ID%=this\ID%
End Function

Function ConnectionCreate%(SERVERID%,TCPSTREAM%,UPDSTREAM%,TCPPORT%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.Connection=ConnectionNew()
	this\ID%		=this\ID%
	this\STATE%		=1
	this\SERVERID%		=SERVERID%
	this\PLAYERID%		=0
	this\NAME$		="New Player"
	this\PING%		=0
	this\LASTALIVE%		=millisecs()
	this\TCPSTREAM%		=TCPSTREAM%
	this\UPDSTREAM%		=UPDSTREAM%
	this\TCPPORT%		=TCPPORT%
	this\UDPPORT%		=0
	this\TCPREADSTATE%	=0
	this\IPSHORT%		=TCPStreamIP(TCPSTREAM%)
	this\IPLONG$		=Dottedip$(this\IPSHORT%)
	this\TCPWAITTIME%	=1
	THIS\CLIENTID%		=0
	THIS\PINGTYPE%		=0
	Return this\id
End Function

Function ConnectionDestroy(Connectionid%)
	;Purpose: Remove object by ID
	;Parameters: Connectionid%=Object's ID
	;Return: None
	ConnectionDelete(Connection_ID[Connectionid%])
End Function

Function ConnectionSet()
	;Purpose: Set Connection Property Values
	;Parameters: Connection Instance and Properties
	;Return: None
End Function

Function ConnectionIndexPush(Connectionid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: Connectionid=Object's ID
	;Return: None
	Connection_Index[Connection_IndexPointer]=Connectionid%
	Connection_IndexPointer=Connection_IndexPointer+1
End Function

Function ConnectionIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	Connection_IndexPointer=Connection_IndexPointer-1
	Return Connection_Index[Connection_IndexPointer]
End Function

Function ConnectionCreateMessage.Bank(ThisVerb, ThisObject, ThisGroup, ThisSubObject, ThisProperty)
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

Function ConnectionProcessStream(ThisBank.Bank)
	FormatByte = BankReadbyte(ThisBank)
	if FormatByte = 55 then
		ThisVerb 	= BankReadbyte(ThisBank)
		ThisObject 	= BankReadbyte(ThisBank)
		Select ThisVerb
			Case NET_SET
				Select ThisObject
					case NET_CONNECTION ConnectionNetSet(ThisBank.Bank)
					case NET_PLAYER PlayerNetSet(ThisBank.Bank)
				End Select
			case NET_RUN
				Select ThisObject
					case NET_PLAYER PlayerNetRUN(ThisBank.Bank)
				End select
		end select
	Endif
End Function

Function ConnectionProcessStreamHandshake(This.Connection, ThisBank.Bank)
	FormatByte = BankReadbyte(ThisBank)
	if FormatByte = 55 then
		ThisVerb 	= BankReadbyte(ThisBank)
		ThisObject 	= BankReadbyte(ThisBank)
		ThisGroup 	= BankReadbyte(ThisBank)
		ThisSubObject 	= BankReadbyte(ThisBank)
		ThisProperty 	= BankReadbyte(ThisBank)
		if ThisVerb = NET_SET and ThisObject = NET_CONNECTION and ThisProperty = NET_CONNECTION_CLIENTID then
			This\ClientID = BankReadbyte(ThisBank)
			This\State=2
		Endif
	Endif
End Function

Function ConnectionNetSet(ThisBank.Bank)
	ThisGroup 	= BankReadbyte(ThisBank)
	ThisSubObject 	= BankReadbyte(ThisBank)
	ThisProperty 	= BankReadbyte(ThisBank)
	This.Connection = Connection_ID[ThisGroup]
	Select ThisProperty
		Case NET_CONNECTION_ID			this\ID%		=BankReadbyte(ThisBank)
		Case NET_CONNECTION_STATE 		this\STATE%		=BankReadbyte(ThisBank)
		Case NET_CONNECTION_SERVERID		this\SERVERID%		=BankReadbyte(ThisBank)
		Case NET_CONNECTION_PLAYERID		this\PLAYERID%		=BankReadbyte(ThisBank)
		Case NET_CONNECTION_NAME		this\NAME$		=BankReadString(ThisBank)
		Case NET_CONNECTION_PING		this\PING%		=BankReadShort(ThisBank)
		Case NET_CONNECTION_LASTALIVE		this\LASTALIVE%		=BankReadint(ThisBank)
		Case NET_CONNECTION_TCPSTREAM		this\TCPSTREAM%		=BankReadint(ThisBank)
		Case NET_CONNECTION_UPDSTREAM		this\UPDSTREAM%		=BankReadint(ThisBank)
		Case NET_CONNECTION_TCPPORT		this\TCPPORT%		=BankReadShort(ThisBank)
		Case NET_CONNECTION_UDPPORT		this\UDPPORT%		=BankReadShort(ThisBank)
		Case NET_CONNECTION_TCPREADSTATE	this\TCPREADSTATE%	=BankReadbyte(ThisBank)
		Case NET_CONNECTION_IPSHORT		this\IPSHORT%		=BankReadint(ThisBank)
		Case NET_CONNECTION_IPLONG		this\IPLONG$		=BankReadstring(ThisBank)
		Case NET_CONNECTION_TCPWAITTIME		this\TCPWAITTIME%	=BankReadshort(ThisBank)
		Case NET_CONNECTION_TIMEOUT		this\TIMEOUT%		=BankReadShort(ThisBank)
		Case NET_CONNECTION_CLIENTID		this\CLIENTID%		=BankReadbyte(ThisBank)
		CASE NET_CONNECTION_PINGTYPE		THIS\PINGTYPE%		=BANKREADBYTE(THISBANK)
	End Select
End Function

Function ConnectionGetUDP(ThisIP% , ThisTCPPort%)
	;Purpose:Finds open UDP for client
	;Parameters: Integer IP, TCP Port
	;Return: None
	local ThisUDPPort% = ThisTCPPort%
	N=CountHostIPs("")
	if dottedIP(thisIP) = "127.0.0.1" then ThisUDPPort%=ThisUDPPort%+1 ;Are we the server
	ThisUDPPort%=ConnectionCheckUDP%(dottedIP(thisIP), ThisUDPPort%)
	return(ThisUDPPort%)
End Function

Function ConnectionCheckUDP%(ThisIPLong$, ThisUDPPort%)
	For This.Connection = each Connection
		if this\UDPPORT% = ThisUDPPort% and this\IPLONG$ =ThisIPLong$ AND this\UDPPORT% >0 then ThisUDPPort%=ThisUDPPort%+1:ThisUDPPort%=ConnectionCheckUDP(ThisIPLong$, ThisUDPPort%)
	Next
	return(ThisUDPPort%)
End Function

Function ConnectionMethod.Connection(this.Connection)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
