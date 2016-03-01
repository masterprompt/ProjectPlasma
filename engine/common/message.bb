;============================
;MESSAGE CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const MESSAGE_MAX%=2000

CONST MESSAGE_V_SET					=1
CONST MESSAGE_V_READ					=2
CONST MESSAGE_V_METHOD					=3

CONST MESSAGE_O_BANK					=1
CONST MESSAGE_O_MESSAGE					=2
CONST MESSAGE_O_LOG					=3
CONST MESSAGE_O_CONSOLE					=4

;============================
;GLOBALS
;============================
Global Message_ID.Message[MESSAGE_MAX%];Primary Key Object Pointer
Global Message_Index%[MESSAGE_MAX%], Message_IndexPointer%;Primary Key ID Management Stack

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type Message
	;Purpose: 
	;Properties:
	Field Content.Bank;
End Type

;============================
;METHODS
;============================
Function MessageStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For Messageloop=MESSAGE_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		MessageIndexPush(Messageloop)
	Next
End Function

Function MessageStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.Message=Each Message
		MessageDelete(this)
	Next
End Function

Function MessageNew.Message()
	;Purpose: CONSTRUCTOR - Creates Message Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: Message Instance
	this.Message=New Message
	this\ID%=0
	this\Content.Bank=BankNew()
	this\id%=MessageIndexPop()
	Message_ID[this\id%]=this
	Return this
End Function

Function MessageDelete(this.Message)
	;Purpose: DESTRUCTOR - Removes Message Instance
	;Parameters: Message Instance
	;Return: None
	Message_ID[this\id]=Null
	MessageIndexPush(this\id%)
	BankDelete(this\Content.Bank)
	Delete this
End Function

Function messageUpdate()
	;Purpose: Main Loop Update. Updates all message Instances.
	;Parameters: None
	;Return: None
	For this.message=Each message
		;Find out who should recieve this message and give it to them
		Select this\ClassTo%
			;Case Message_Log 		LogMessage(this.message)
			;Case Message_Server 		ServerMessage(this.Message)
			;Case Message_Connection 	ConnectionMessage(this.message)
			;Case Message_Client 		ClientMessage(this.message)
			;Case Message_Message 		MessageMessage(this.message)
			;Case Message_Bank 		BankMessage(this.message)
			;case Message_Console		ConsoleMessage(this.message)
			;case Message_Player		PlayerMessage(this.message)
			;case Message_ControlMap		ControlMapMessage(this.message)
			;case 0;Send to all, then Delete
			;	LogMessage(this.message)
			;	ServerMessage(this.Message)
			;	ConnectionMessage(this.message)
			;	ClientMessage(this.message)
			;	MessageMessage(this.message)
			;	BankMessage(this.message)
			;	ConsoleMessage(this.message)
			;	PlayerMessage(this.message)
			;	ControlMapMessage(this.message)
			;	MessageDelete(This.Message)
		End Select
	Next
End Function

Function MessageWrite(this.Message,Messagefile)
	;Purpose: Writes Message Property Values to File
	;Parameters: Message Instance, Messagefile=filehandle
	;Return: None
	WriteInt(Messagefile,this\ID%)
	BankWrite(this\Content.Bank,Messagefile)
End Function

Function MessageRead.Message(Messagefile)
	;Purpose: Reads File To Message Property Values
	;Parameters: Messagefile=filehandle
	;Return: Message Instance
	this.Message=New Message
	this\ID%=ReadInt(Messagefile)
	this\Content.Bank=BankRead(Messagefile)
	MessageIndexPop()
	Message_ID[this\id%]=this
	Return this
End Function

Function MessageSave(Messagefilename$="Message.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: Messagefilename$= Name of File
	;Return: None
	Messagefile=WriteFile(Messagefilename$)
	For this.Message= Each Message
		MessageWrite(this,Messagefile)
	Next
	CloseFile(Messagefile)
End Function

Function MessageOpen(Messagefilename$="Message.dat")
	;Purpose: Opens a Object Property file
	;Parameters: Messagefilename$=Name of Property File
	;Return: None
	Messagefile=ReadFile(Messagefilename$)
	Repeat
		MessageRead(Messagefile)
	Until Eof(Messagefile)
	CloseFile(Messagefile)
End Function

Function MessageCopy.Message(this.Message)
	;Purpose: Creates a Copy of a Message Instance
	;Parameters: Message Instance
	;Return: Copy of Message Instance
	copy.Message=MessageNew()
	copy\ID%=this\ID%
	copy\Content.Bank=BankCopy(this\Content.Bank)
	Return copy
End Function

Function MessageMimic(this.Message,mimic.Message)
	;Purpose: Copies Property Values from one Message Instance To another
	;Parameters: Message Instance
	;Return: None
	mimic\ID%=this\ID%
	BankMimic(this\Content.Bank,mimic\Content.Bank)
End Function

Function MessageCreate%(MessageID%,MessageContent.Bank)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.Message=MessageNew()
	this\ID%=MessageID%
	this\Content.Bank=MessageContent.Bank
	Return this\id
End Function

Function MessageDestroy(Messageid%)
	;Purpose: Remove object by ID
	;Parameters: Messageid%=Object's ID
	;Return: None
	MessageDelete(Message_ID[Messageid%])
End Function

Function MessageSet(this.Message,MessageID%,MessageContent.Bank)
	;Purpose: Set Message Property Values
	;Parameters: Message Instance and Properties
	;Return: None
	this\ID%=MessageID%
	this\Content.Bank=MessageContent.Bank
End Function

Function MessageIndexPush(Messageid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: Messageid=Object's ID
	;Return: None
	Message_Index[Message_IndexPointer]=Messageid%
	Message_IndexPointer=Message_IndexPointer+1
End Function

Function MessageIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	Message_IndexPointer=Message_IndexPointer-1
	Return Message_Index[Message_IndexPointer]
End Function

Function MessageMessage(ThisMessage.Message)
	;Purpose: Processes Messages for this class
	;Parameters: Message Instance
	;Return: None
	if ThisMessage\ClassTo% <> 0 then MessageDelete(this.message)
End Function

Function MessageMethod.Message(this.Message)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
