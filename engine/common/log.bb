;============================
;LOG CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const LOG_MAX%=10

;============================
;GLOBALS
;============================
Global log_ID.log[LOG_MAX%];Primary Key Object Pointer
Global log_Index%[LOG_MAX%], log_IndexPointer%;Primary Key ID Management Stack

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type log
	;Purpose: 
	;Properties:
	Field ID%;
	Field logfile%;
	Field logname$;
End Type

;============================
;METHODS
;============================
Function logStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For logloop=LOG_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		logIndexPush(logloop)
	Next
End Function

Function logStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.log=Each log
		logDelete(this)
	Next
End Function

Function logNew.Log(logfileName$="")
	;Purpose: CONSTRUCTOR - Creates log Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: log Instance
	this.log=New log
	this\ID%=0
	If logfileName$="" Then 
		For A = 1 To 1000
			fName$ = A
			fNamef$ = "log/"+ (Left("L00000", (Len("L00000")-Len(fName$))) + fName$) + ".log"
			If FileType(fNamef$) = 0 Then Exit
		Next
		logfileName$=fNamef$
	EndIf
	this\logfile%=WriteFile(logfileName$)
	this\logname$=logfileName$
	this\id%=logIndexPop()
	log_ID[this\id%]=this
	logOut("Logging Engine Started ->")
	Return this
End Function

Function logDelete(this.log)
	;Purpose: DESTRUCTOR - Removes log Instance
	;Parameters: log Instance
	;Return: None
	logOut("Logging Engine Stopped ->")
	If this\logfile<>0 Then CloseFile(this\logfile)
	this\logfile=0
	this\logname$="" 
	log_ID[this\id]=Null
	logIndexPush(this\id%)
	Delete this
End Function

Function logUpdate()
	;Purpose: Main Loop Update. Updates all log Instances.
	;Parameters: None
	;Return: None
	For this.log=Each log
		;Do stuff to 'this' here!
	Next
End Function

Function logWrite(this.log,logfile)
	;Purpose: Writes log Property Values to File
	;Parameters: log Instance, logfile=filehandle
	;Return: None
	WriteInt(logfile,this\ID%)
	WriteString(logfile,this\logname$)
End Function

Function logRead.log(logfile)
	;Purpose: Reads File To log Property Values
	;Parameters: logfile=filehandle
	;Return: log Instance
	this.log=New log
	this\ID%=ReadInt(logfile)
	this\logname$=ReadString(logfile)
	logIndexPop()
	log_ID[this\id%]=this
	Return this
End Function

Function logSave(logfilename$="log.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: logfilename$= Name of File
	;Return: None
	logfile=WriteFile(logfilename$)
	For this.log= Each log
		logWrite(this,logfile)
	Next
	CloseFile(logfile)
End Function

Function logOpen(logfilename$="log.dat")
	;Purpose: Opens a Object Property file
	;Parameters: logfilename$=Name of Property File
	;Return: None
	logfile=ReadFile(logfilename$)
	Repeat
		logRead(logfile)
	Until Eof(logfile)
	CloseFile(logfile)
End Function

Function logCopy.log(this.log)
	;Purpose: Creates a Copy of a log Instance
	;Parameters: log Instance
	;Return: Copy of log Instance
	copy.log=logNew()
	copy\ID%=this\ID%
	copy\logfile%=this\logfile%
	copy\logname$=this\logname$
	Return copy
End Function

Function logMimic(this.log,mimic.log)
	;Purpose: Copies Property Values from one log Instance To another
	;Parameters: log Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\logfile%=this\logfile%
	mimic\logname$=this\logname$
End Function

Function logCreate%(logID%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.log=logNew()
	this\ID%=logID%
	Return this\id
End Function

Function logDestroy(logid%)
	;Purpose: Remove object by ID
	;Parameters: logid%=Object's ID
	;Return: None
	logDelete(log_ID[logid%])
End Function

Function logSet(this.Log,logID%,loglogfile%,loglogname$)
	;Purpose: Set log Property Values
	;Parameters: log Instance and Properties
	;Return: None
	this\ID%=logID%
	this\logfile%=loglogfile%
	this\logname$=loglogname$
End Function

Function logIndexPush(logid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: logid=Object's ID
	;Return: None
	log_Index[log_IndexPointer]=logid%
	log_IndexPointer=log_IndexPointer+1
End Function

Function logIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	log_IndexPointer=log_IndexPointer-1
	Return log_Index[log_IndexPointer]
End Function

Function logOut(LogInfo$="")
	;Purpose: Log information to the log file
	;Parameters: Instance of log, log info
	;Return: None
	For this.Log=Each Log
		WriteLine(this\logfile%,"L"+CurrentDate()+"  "+CurrentTime()+">>"+LogInfo$)
	Next
	ConsoleNew(LogInfo$)
End Function

Function logMethod.log(this.log)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
