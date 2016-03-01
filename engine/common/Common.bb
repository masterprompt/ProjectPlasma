;============================
;Common CLASS
;============================

;============================
;INCLUDES
;============================
Include "common\bank.bb"
Include "common\log.bb"
;Include "common\message.bb"

;============================
;METHODS
;============================
Function CommonStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	BankStart()
	;MessageStart()
	LogStart()
	LogNew()
End Function

Function CommonStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	LogStop()
	;MessageStop()
	BankStop()
End Function

Function CommonUpdate()
	;Purpose: Main Loop Update. Updates all Common Instances.
	;Parameters: None
	;Return: None
	BankUpdate()
	;MessageUpdate()
	LogUpdate()
End Function
