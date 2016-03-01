;=============================
;LIGHT CLASS
;Generated with TypeWriterIV
;=============================

;=============================
;INCLUDES
;=============================

;=============================
;CONSTANTS
;=============================
Const LIGHT_MAX%=8
Const LIGHT_TYPE_OMNI%=1
Const LIGHT_TYPE_SPOT%=2
Const LIGHT_STATE_OFF=0
Const LIGHT_STATE_ON=1
Const LIGHT_STATE_TURNINGOFF=2

;=============================
;GLOBALS
;=============================
Global light_stackPTR%; Pointer for light_stack array

;=============================
;ARRAYS
;=============================
Dim light_stack.light(LIGHT_MAX%)


;=============================
;OBJECT
;=============================
Type light
	;Purpose: 
	;Properties:
	Field entity%;
	Field lighttype%;
	Field range#;
	Field innerangle#;
	Field outerangle#;
	Field red#;
	Field green#;
	Field blue#;
	Field priority%
	Field lifeCTR%;
	Field state%;
End Type

;=============================
;METHODS
;=============================
Function lightStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For lightloop=1 To LIGHT_MAX
		lightStackPush(lightNew())	
	Next		
End Function

Function lightStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.light=Each light
		lightDelete(this)
	Next
End Function

Function lightNew.light()
	;Purpose: CONSTRUCTOR - Creates light Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: light Instance
	this.light=New light
	this\entity%=0
	this\lighttype%=0
	this\range#=0.0
	this\innerangle#=0.0
	this\outerangle#=0.0
	this\red#=0.0
	this\green#=0.0
	this\blue#=0.0
	this\lifeCTR%=0
	this\state%=0
	Return this
End Function

Function lightDelete(this.light)
	;Purpose: DESTRUCTOR - Removes light Instance
	;Parameters: light Instance
	;Return: None
	this\blue#=0.0
	this\green#=0.0
	this\red#=0.0
	this\outerangle#=0.0
	this\innerangle#=0.0
	this\range#=0.0
	Freeentity this\entity%
	Delete this
End Function

Function lightUpdate()
	;Purpose: Main Loop Update. Updates all light Instances.
	;Parameters: None
	;Return: None
	For this.light=Each light
		Select this\state
			Case LIGHT_STATE_ON
				this\lifeCTR=this\lifeCTR-1
				If Not this\lifeCTR this\state= LIGHT_STATE_TURNINGOFF
			Case LIGHT_STATE_TURNINGOFF
				If this\lighttype=LIGHT_TYPE_OMNI
					recyclerOmniLightPush(recycler_ID[recycler_OmniLightID%],this\entity)
				Else
					recyclerSpotLightPush(recycler_ID[recycler_SpotLightID%],this\entity)
				EndIf
				lightStackPush(this)
				this\state=LIGHT_STATE_OFF
		End Select
	Next
End Function

Function lightWrite(this.light,lightfile)
	;Purpose: Writes light Property Values to File
	;Parameters: light Instance, lightfile=filehandle
	;Return: None
	WriteInt(lightfile,this\entity%)
	WriteInt(lightfile,this\lighttype%)
	WriteFloat(lightfile,this\range#)
	WriteFloat(lightfile,this\innerangle#)
	WriteFloat(lightfile,this\outerangle#)
	WriteFloat(lightfile,this\red#)
	WriteFloat(lightfile,this\green#)
	WriteFloat(lightfile,this\blue#)
	WriteInt(lightfile,this\lifeCTR%)
	WriteInt(lightfile,this\state%)
End Function

Function lightRead.light(lightfile)
	;Purpose: Reads File To light Property Values
	;Parameters: lightfile=filehandle
	;Return: light Instance
	this.light=New light
	this\entity%=ReadInt(lightfile)
	this\lighttype%=ReadInt(lightfile)
	this\range#=ReadFloat(lightfile)
	this\innerangle#=ReadFloat(lightfile)
	this\outerangle#=ReadFloat(lightfile)
	this\red#=ReadFloat(lightfile)
	this\green#=ReadFloat(lightfile)
	this\blue#=ReadFloat(lightfile)
	this\lifeCTR%=ReadInt(lightfile)
	this\state%=ReadInt(lightfile)
	Return this
End Function

Function lightSave(lightfilename$="light.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: lightfilename$= Name of File
	;Return: None
	lightfile=WriteFile(lightfilename$)
	For this.light= Each light
		lightWrite(this,lightfile)
	Next
	CloseFile(lightfile)
End Function

Function lightOpen(lightfilename$="light.dat")
	;Purpose: Opens a Object Property file
	;Parameters: lightfilename$=Name of Property File
	;Return: None
	lightfile=ReadFile(lightfilename$)
	Repeat
		lightRead(lightfile)
	Until Eof(lightfile)
	CloseFile(lightfile)
End Function

Function lightCopy.light(this.light)
	;Purpose: Creates a Copy of a light Instance
	;Parameters: light Instance
	;Return: Copy of light Instance
	copy.light=lightNew()
	copy\entity%=this\entity%
	copy\lighttype%=this\lighttype%
	copy\range#=this\range#
	copy\innerangle#=this\innerangle#
	copy\outerangle#=this\outerangle#
	copy\red#=this\red#
	copy\green#=this\green#
	copy\blue#=this\blue#
	copy\lifeCTR%=this\lifeCTR%;
	copy\state%=this\state%
	Return copy
End Function

Function lightMimic(this.light,mimic.light)
	;Purpose: Copies Property Values from one light Instance To another
	;Parameters: light Instance
	;Return: None
	mimic\entity%=this\entity%
	mimic\lighttype%=this\lighttype%
	mimic\range#=this\range#
	mimic\innerangle#=this\innerangle#
	mimic\outerangle#=this\outerangle#
	mimic\red#=this\red#
	mimic\green#=this\green#
	mimic\blue#=this\blue#
	mimic\lifeCTR%=this\lifeCTR%;
	mimic\state%=this\state%
End Function

Function lightSet(this.light,lightentity%,lightlighttype%,lightlightrange#,lightinnerangle#,lightouterangle#,lightred#,lightgreen#,lightblue#,lightlifeCTR%,lightstate%)
	;Purpose: Set light Property Values
	;Parameters: light Instance and Properties
	;Return: None
	this\entity%=lightentity%
	this\lighttype%=lightlighttype%
	this\range#=lightrange#
	this\innerangle#=lightinnerangle#
	this\outerangle#=lightouterangle#
	this\red#=lightred#
	this\green#=lightgreen#
	this\blue#=lightblue#
	this\lifeCTR%=lightlifeCTR%;
	this\state%=lightstate%
	LightRange(this\entity,.5);this\range)
	LightColor(this\entity,this\red#,this\green#,this\blue#) 
	if this\lighttype=LIGHT_STATE_SPOT LightConeAngles(this\entity,this\innerangle#,this\outerangle# )
End Function

Function lightStackPush(this.light)
	;Purpose: Pushes light object into light_stack
	;Parameters: TBD
	;Return: TBD
	light_stack(light_stackPTR%)=this
	light_stackPTR%=light_stackPTR%+1	
End Function

Function lightStackPop.light()
	;Purpose: Pops light object out of light_stack
	;Parameters: TBD
	;Return: TBD
	light_stackPTR%=light_stackPTR%-1	
	Return light_stack(light_stackPTR%)	
End Function


Function lightMethod.light(this.light)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function