;============================
;CONSOLE CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const CONSOLE_MAX%=500

;============================
;GLOBALS
;============================
Global Console_Index%;Primary Key ID Management Counter
Global Console_Cnt%;Count of records
Global Console_Line$;Current Console Input Line
Global Console_Enabled%=1;Display Console
Global Console_LineDisplay%=5;Lines to display
Global Console_CurserFlag%=0;Curser Flag
Global Console_CurserTime%=200;Blink Time for Curser
Global Console_CurserLast%;Last Blink Time

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type Console
	;Purpose: 
	;Properties:
	Field ID%;
	Field Content$;
End Type

;============================
;METHODS
;============================
Function ConsoleStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
End Function

Function ConsoleStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.Console=Each Console
		ConsoleDelete(this)
	Next
End Function

Function ConsoleNew.Console(Content$="")
	;Purpose: CONSTRUCTOR - Creates Console Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: Console Instance
	this.Console=New Console
	insert this before first console
	this\ID%=0
	this\Content$=Content$
	Console_Index=Console_Index+1
	this\id%=Console_Index
	Console_Cnt%=Console_Cnt%+1
	if Console_Cnt% > CONSOLE_MAX% then
		ToDelete=Console_Max-Console_Cnt
		For A = 1 to ToDelete
			this.Console = last Console
			Delete(This.Console)
		Next
	Endif
	Return this
End Function

Function ConsoleDelete(this.Console)
	;Purpose: DESTRUCTOR - Removes Console Instance
	;Parameters: Console Instance
	;Return: None
	this\Content$=""
	Console_Cnt%=Console_Cnt%-1
	Delete this
End Function

Function ConsoleUpdate()
	;Purpose: Main Loop Update. Updates all Console Instances.
	;Parameters: None
	;Return: None
	if Console_Enabled% then
		controlmap_enabled%=0
		ConsoleGrabInputs()
		if Millisecs() - Console_CurserLast% > Console_CurserTime% then
			Console_CurserLast% = MillisecS()
			Console_CurserFlag = 1-Console_CurserFlag
		Endif
		ConsoleDisplay()
		if keydown(59) and Console_Enabled% = 1 then Console_Enabled%=2
		if keydown(59)=0 and Console_Enabled%=2 then 
			Console_Enabled%=0
		endif
		if keydown(59)=0 and Console_Enabled%=-1 then 
			Console_Enabled%=1
		endif
	else
		controlmap_enabled%=1
		if keydown(59) and Console_Enabled% = 0 then 
			Console_Enabled%=-1
			FlushKeys()
		endif
	Endif

End Function

Function ConsoleWrite(this.Console,Consolefile)
	;Purpose: Writes Console Property Values to File
	;Parameters: Console Instance, Consolefile=filehandle
	;Return: None
	WriteInt(Consolefile,this\ID%)
	WriteString(Consolefile,this\Content$)
End Function

Function ConsoleRead.Console(Consolefile)
	;Purpose: Reads File To Console Property Values
	;Parameters: Consolefile=filehandle
	;Return: Console Instance
	this.Console=New Console
	this\ID%=ReadInt(Consolefile)
	this\Content$=ReadString(Consolefile)
	Return this
End Function

Function ConsoleSave(Consolefilename$="Console.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: Consolefilename$= Name of File
	;Return: None
	Consolefile=WriteFile(Consolefilename$)
	For this.Console= Each Console
		ConsoleWrite(this,Consolefile)
	Next
	CloseFile(Consolefile)
End Function

Function ConsoleOpen(Consolefilename$="Console.dat")
	;Purpose: Opens a Object Property file
	;Parameters: Consolefilename$=Name of Property File
	;Return: None
	Consolefile=ReadFile(Consolefilename$)
	Repeat
		ConsoleRead(Consolefile)
	Until Eof(Consolefile)
	CloseFile(Consolefile)
End Function

Function ConsoleCopy.Console(this.Console)
	;Purpose: Creates a Copy of a Console Instance
	;Parameters: Console Instance
	;Return: Copy of Console Instance
	copy.Console=ConsoleNew()
	copy\ID%=this\ID%
	copy\Content$=this\Content$
	Return copy
End Function

Function ConsoleMimic(this.Console,mimic.Console)
	;Purpose: Copies Property Values from one Console Instance To another
	;Parameters: Console Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\Content$=this\Content$
End Function

Function ConsoleSet(this.Console,ConsoleID%,ConsoleContent$)
	;Purpose: Set Console Property Values
	;Parameters: Console Instance and Properties
	;Return: None
	this\ID%=ConsoleID%
	this\Content$=ConsoleContent$
End Function

Function ConsoleGrabInputs()
	;Purpose: To get inputs to the console Line
	;Parameters: None
	;Return: None
	Local ThisKey = GetKey()
	;32-126 = characters
	;13 - CR
	;8 - Backspace
	if ThisKey >=32 and ThisKey <=126 then
		Console_Line$=Console_Line$+chr(ThisKey)
	Elseif ThisKey = 13 and Console_Line$ <> ""
		ConsoleProcessCommandGeneric(Console_Line$)
		;ConsoleLogData(">"+Console_Line$)
		Console_Line$=""
	elseif ThisKey = 8
		if len(Console_Line$) > 0 then Console_Line$=left(Console_Line$,len(Console_Line$)-1)
	Endif
	;Console_Line$
End Function

Function ConsoleDisplay()
	;Purpose: Displays Console
	;Parameters: None
	;Return: None
	Local CharOffsetH = 14
	Local DrawStartY =Console_LineDisplay%*CharOffsetH
	Local ConsoleCnt=0,Curser$
	For This.Console = each Console
		ConsoleCnt=ConsoleCnt+1
		if ConsoleCnt >Console_LineDisplay then exit
		text(5,DrawStartY - (ConsoleCnt*CharOffsetH),this\Content$)
	Next
	If Console_CurserFlag then Curser$ = "_" else Curser$ = " "
	color(40,40,50)
	rect(0,DrawStartY,GraphicsWidth(),CharOffsetH,1)
	color(255,255,255)
	Text 5,DrawStartY,Console_Line$+Curser$
End Function

Function ConsoleProcessCommandGeneric(ThisCommand$)
	;Purpose: Generic Command Processing (to be an engine or scripter)
	;Parameters: Command
	;Return: None
	If left(upper(ThisCommand$),len("QUIT")) = "QUIT" then
		game_State=GAME_STATE_EXIT
	elseif left(upper(ThisCommand$),len("DISCONNECT")) = "DISCONNECT" then

	elseif left(upper(ThisCommand$),len("CONNECT ")) = "CONNECT " then
		;Send a connection message
		ThisData$ = Right(ThisCommand$,len(ThisCommand$)-len("CONNECT "))
		ClientCreate%(ThisData$)
	elseif left(upper(ThisCommand$),len("STARTSERVER")) = "STARTSERVER" then
		ServerNew()
	elseif left(upper(ThisCommand$),len("SAY ")) = "SAY " then
		ThisData$ = Right(ThisCommand$,len(ThisCommand$)-len("SAY "))
	elseif left(upper(ThisCommand$),len("NAME ")) = "NAME " then
		ThisData$ = Right(ThisCommand$,len(ThisCommand$)-len("NAME "))
	else
		LogOut("Unkonwn Command>"+ThisCommand$)
	endif
End Function

Function ConsoleMethod.Console(this.Console)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
