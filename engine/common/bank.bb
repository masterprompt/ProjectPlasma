;============================
;BANK CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const BANK_MAX%=2000

;============================
;GLOBALS
;============================
Global bank_ID.bank[BANK_MAX%];Primary Key Object Pointer
Global bank_Index%[BANK_MAX%], bank_IndexPointer%;Primary Key ID Management Stack
Global BANK_DataIn%,BANK_DataOut%
Global BANK_SampleTime
Global BANK_DI%, BANK_DO%
;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type bank
	;Purpose: 
	;Properties:
	Field ID%;
	Field bankhandle%;
	Field readptr%;
	Field writeptr%;
	Field size%;
End Type

;============================
;METHODS
;============================
Function bankStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For bankloop=BANK_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		bankIndexPush(bankloop)
	Next
End Function

Function bankStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.bank=Each bank
		bankDelete(this)
	Next
End Function

Function bankNew.bank(ThisSize=0)
	;Purpose: CONSTRUCTOR - Creates bank Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: bank Instance
	this.bank=New bank
	this\ID%=0
	this\size%=4+ThisSize
	this\bankhandle%=CreateBank(this\size%)
	this\readptr%=4
	this\writeptr%=4
	PokeInt(this\bankhandle%,0,this\size%)
	this\id%=bankIndexPop()
	bank_ID[this\id%]=this
	Return this
End Function

Function bankDelete(this.bank)
	;Purpose: DESTRUCTOR - Removes bank Instance
	;Parameters: bank Instance
	;Return: None
	FreeBank(this\bankhandle%)
	bank_ID[this\id]=Null
	bankIndexPush(this\id%)
	Delete this
End Function

Function bankUpdate()
	;Purpose: Main Loop Update. Updates all bank Instances.
	;Parameters: None
	;Return: None
	ThisTimeFrame = Millisecs() -BANK_SampleTime
	if ThisTimeFrame >=1000 then
		BANK_DataIn% = BANK_DI%
		BANK_DataOut% = BANK_DO%
		BANK_DI%=0
		BANK_DO%=0
		BANK_SampleTime=millisecs()
	Endif
End Function

Function bankWrite(this.bank,bankfile)
	;Purpose: Writes bank Property Values to File
	;Parameters: bank Instance, bankfile=filehandle
	;Return: None
	WriteInt(bankfile,this\ID%)
	WriteInt(bankfile,this\readptr%)
	WriteInt(bankfile,this\writeptr%)
	WriteInt(bankfile,this\size%)
	WriteBytes(this\bankhandle%,BankFile,0,this\size%)	
End Function

Function bankRead.bank(bankfile)
	;Purpose: Reads File To bank Property Values
	;Parameters: bankfile=filehandle
	;Return: bank Instance
	this.bank=New bank
	this\ID%=ReadInt(bankfile)
	this\readptr%=ReadInt(bankfile)
	this\writeptr%=ReadInt(bankfile)
	this\size%=ReadInt(bankfile)	
	this\bankhandle%=CreateBank(this\size%)
	ReadBytes(this\bankhandle%,bankfile,0,this\size%)
	bankIndexPop()
	bank_ID[this\id%]=this
	Return this
End Function

Function bankSave(bankfilename$="bank.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: bankfilename$= Name of File
	;Return: None
	bankfile=WriteFile(bankfilename$)
	For this.bank= Each bank
		bankWrite(this,bankfile)
	Next
	CloseFile(bankfile)
End Function

Function bankOpen(bankfilename$="bank.dat")
	;Purpose: Opens a Object Property file
	;Parameters: bankfilename$=Name of Property File
	;Return: None
	bankfile=ReadFile(bankfilename$)
	Repeat
		bankRead(bankfile)
	Until Eof(bankfile)
	CloseFile(bankfile)
End Function

Function bankCopy.bank(this.bank)
	;Purpose: Creates a Copy of a bank Instance
	;Parameters: bank Instance
	;Return: Copy of bank Instance
	copy.bank=bankNew()
	copy\ID%=this\ID%
	copy\bankhandle%=CreateBank(this\size%)
	CopyBank(this\bankhandle%,0,copy\bankhandle%,0,this\size%)
	copy\readptr%=this\readptr%
	copy\writeptr%=this\writeptr%
	copy\size%=this\size%
	Return copy
End Function

Function bankMimic(this.bank,mimic.bank)
	;Purpose: Copies Property Values from one bank Instance To another
	;Parameters: bank Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\bankhandle%=this\bankhandle%
	mimic\readptr%=this\readptr%
	mimic\writeptr%=this\writeptr%
	mimic\size%=this\size%
End Function

Function bankCreate%(bankID%,bankbankhandle%,bankreadptr%,bankwriteptr%,banksize%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.bank=bankNew()
	this\ID%=bankID%
	this\bankhandle%=bankbankhandle%
	this\readptr%=bankreadptr%
	this\writeptr%=bankwriteptr%
	this\size%=banksize%
	Return this\id
End Function

Function bankDestroy(bankid%)
	;Purpose: Remove object by ID
	;Parameters: bankid%=Object's ID
	;Return: None
	bankDelete(bank_ID[bankid%])
End Function

Function bankSet(this.bank,bankID%,bankbankhandle%,bankreadptr%,bankwriteptr%,banksize%)
	;Purpose: Set bank Property Values
	;Parameters: bank Instance and Properties
	;Return: None
	this\ID%=bankID%
	this\bankhandle%=bankbankhandle%
	this\readptr%=bankreadptr%
	this\writeptr%=bankwriteptr%
	this\size%=banksize%
End Function

Function bankIndexPush(bankid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: bankid=Object's ID
	;Return: None
	bank_Index[bank_IndexPointer]=bankid%
	bank_IndexPointer=bank_IndexPointer+1
End Function

Function bankIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	bank_IndexPointer=bank_IndexPointer-1
	Return bank_Index[bank_IndexPointer]
End Function

Function BankResetRead(this.bank)
	;Purpose: Reset Read Pointer
	;Parameters: bank Instance
	;Return: None
	this\readptr%=4
End Function

Function BankReset(this.bank)
	;Purpose: Reset Read and Write Pointer (deletes bank data)
	;Parameters: bank Instance
	;Return: None
	this\readptr%=4
	this\writeptr%=4
	this\size%=4
	ResizeBank(this\bankhandle%,this\size%)
	PokeInt(this\bankhandle%,0,this\size%)
End Function

Function BankWriteByte(this.Bank, ThisData%, ThisOffset%=-1)
	;Purpose: Writes Byte to a bank
	;Parameters: bank Instance, Byte
	;Return: None
	if ThisOffset% = -1 then
		Local DataOffset=1
		this\size%=this\size%+DataOffset
		ResizeBank(this\bankhandle%,this\size%)
		PokeByte(this\bankhandle%,this\writeptr%,ThisData%)
		this\writeptr%=this\writeptr%+DataOffset
		PokeInt(this\bankhandle%,0,this\size%)
	else
		PokeByte(this\bankhandle%,ThisOffset%,ThisData%)
	endif
End Function

Function BankWriteShort(this.Bank, ThisData%, ThisOffset%=-1)
	;Purpose: Writes Short to a bank
	;Parameters: bank Instance, Short
	;Return: None
	if ThisOffset% = -1 then
		Local DataOffset=2
		this\size%=this\size%+DataOffset
		ResizeBank(this\bankhandle%,this\size%)
		PokeShort(this\bankhandle%,this\writeptr%,ThisData%)
		this\writeptr%=this\writeptr%+DataOffset
		PokeInt(this\bankhandle%,0,this\size%)
	else
		Pokeshort(this\bankhandle%,ThisOffset%,ThisData%)
	endif
End Function

Function BankWriteInt(this.Bank, ThisData%, ThisOffset%=-1)
	;Purpose: Writes Int to a bank
	;Parameters: bank Instance, Int
	;Return: None
	if ThisOffset% = -1 then
		Local DataOffset=4
		this\size%=this\size%+DataOffset
		ResizeBank(this\bankhandle%,this\size%)
		PokeInt(this\bankhandle%,this\writeptr%,ThisData%)
		this\writeptr%=this\writeptr%+DataOffset
		PokeInt(this\bankhandle%,0,this\size%)
	else
		Pokeint(this\bankhandle%,ThisOffset%,ThisData%)
	endif
End Function

Function BankWriteFloat(this.Bank, ThisData#, ThisOffset%=-1)
	;Purpose: Writes Byte to a bank
	;Parameters: bank Instance, Byte
	;Return: None
	if ThisOffset% = -1 then
		Local DataOffset=4
		this\size%=this\size%+DataOffset
		ResizeBank(this\bankhandle%,this\size%)
		PokeFloat(this\bankhandle%,this\writeptr%,ThisData#)
		this\writeptr%=this\writeptr%+DataOffset
		PokeInt(this\bankhandle%,0,this\size%)
	else
		Pokefloat(this\bankhandle%,ThisOffset%,ThisData#)
	endif
End Function

Function BankWriteString(this.Bank, ThisData$)
	;Purpose: Writes String to a bank
	;Parameters: bank Instance, String
	;Return: None
	Local DataOffset,A
	this\size%=this\size%+Len(ThisData$)+4
	ResizeBank(this\bankhandle%,this\size%)
	PokeInt(this\bankhandle%,this\writeptr%,Len(ThisData$))
	this\writeptr%=this\writeptr%+4
	For A = 1 To Len(ThisData$)
		PokeByte(this\bankhandle%,this\writeptr%,Asc(Mid(ThisData$,A,1)))
		this\writeptr%=this\writeptr%+1
	Next
	PokeInt(this\bankhandle%,0,this\size%)
End Function

Function BankReadByte%(this.bank, ThisOffset%=-1)
	;Purpose: Reads Byte From a Bank
	;Parameters: bank Instance
	;Return: Byte
	Local DataOffset=1
	if ThisOffset% = -1 then
		if (this\readptr+DataOffset) <= this\size% then
			this\readptr%=this\readptr%+DataOffset
			Return(PeekByte(this\bankhandle%,this\readptr%-DataOffset))
		endif
	elseif (ThisOffset%+DataOffset) <= this\size% then
		Return(PeekByte(this\bankhandle%,ThisOffset%))
	endif
	return(0)
End Function

Function BankReadShort%(this.bank, ThisOffset%=-1)
	;Purpose: Reads Byte From a Bank
	;Parameters: bank Instance
	;Return: Byte
	Local DataOffset=2
	if ThisOffset% = -1 then
		if (this\readptr+DataOffset) <= this\size% then
			this\readptr%=this\readptr%+DataOffset
			Return(PeekShort(this\bankhandle%,this\readptr%-DataOffset))
		endif
	elseif (ThisOffset%+DataOffset) <= this\size% then
		Return(PeekShort(this\bankhandle%,ThisOffset%))
	endif
	return(0)
End Function

Function BankReadInt%(this.bank, ThisOffset%=-1)
	;Purpose: Reads Byte From a Bank
	;Parameters: bank Instance
	;Return: Byte
	Local DataOffset=4
	if ThisOffset% = -1 then
		if (this\readptr+DataOffset) <= this\size% then
			this\readptr%=this\readptr%+DataOffset
			Return(PeekInt(this\bankhandle%,this\readptr%-DataOffset))
		endif
	elseif (ThisOffset%+DataOffset) <= this\size% then
		Return(Peekint(this\bankhandle%,ThisOffset%))
	endif
	return(0)
End Function

Function BankReadFloat#(this.bank, ThisOffset%=-1)
	;Purpose: Reads Byte From a Bank
	;Parameters: bank Instance
	;Return: Byte
	Local DataOffset=4
	if ThisOffset% = -1 then
		if (this\readptr+DataOffset) <= this\size% then
			this\readptr%=this\readptr%+DataOffset
			Return(PeekFloat(this\bankhandle%,this\readptr%-DataOffset))
		endif
	elseif (ThisOffset%+DataOffset) <= this\size% then
		Return(PeekFloat(this\bankhandle%,ThisOffset%))
	endif
	return(0)
End Function

Function BankEOF(This.Bank)
	;Purpose: Ckeck for Bank EOF
	;Parameters: bank Instance
	;Return: EOF Flag
	If this\readptr >=this\size% Then Return(1)
	return(0)
End Function

Function BankReadString$(this.bank, ThisOffset%=-1)
	;Purpose: Reads Byte From a Bank
	;Parameters: bank Instance
	;Return: Byte
	Local DataOffset,A,ThisString$=""
	if (this\readptr+4) <= this\size% then
		DataOffset = PeekInt(this\bankhandle%,this\readptr%)
		this\readptr%=this\readptr%+4
		For A = 1 To DataOffset
			if (this\readptr+1) <= this\size% then
				ThisString$=ThisString$+Chr(PeekByte(this\bankhandle%,this\readptr%))
				this\readptr%=this\readptr%+1
			endif
		Next
		Return(ThisString$)
	endif
End Function

Function BankSendStream(this.bank, ThisStream, ThisSize=0, ThisIP%=0, ThisPort%=0)
	;Purpose: Writes Bank to Stream
	;Parameters: bank Instance, Streamhandle
	;Return: None
	if ThisSize >0 then
		this\size%=ThisSize+4
		ResizeBank(this\bankhandle%,this\size%)
		PokeInt(this\bankhandle%,0,this\size%)
	endif
	WriteBytes(this\bankhandle%,ThisStream,4,this\size%-4)
	BANK_Do%=BANK_Do%+this\size%
	if ThisIP% <> 0 and ThisPort% <> 0 then sendudpmsg(ThisStream,ThisIP%,ThisPort%)
	BankDelete(This)
End Function

Function BankRecStream(this.bank, ThisStream, ThisStreamSize)
	;Purpose: Reads Bank From Stream
	;Parameters: bank Instance, Streamhandle
	;Return: None
	local ThisAvail=0
	ThisAvail = readavail(ThisStream)
	if thisAvail >= ThisStreamSize then
		this\readptr%=4
		this\writeptr%=4
		this\size%=ThisStreamSize+4
		ResizeBank(this\bankhandle%,this\size%)
		PokeInt(this\bankhandle%,0,this\size%)
		ReadBytes(this\bankhandle%,ThisStream,4,this\size%-4)
		BANK_DI%=BANK_DI%+ThisStreamSize
		return(1);Sucess
	else
		BankReset(this.bank)
		Return(0);Failure
	endif
End Function

Function bankMethod.bank(this.bank)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
