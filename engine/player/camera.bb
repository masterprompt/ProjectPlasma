;============================
;CAMERA CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const CAMERA_MAX%=20

;============================
;GLOBALS
;============================
Global camera_ID.camera[CAMERA_MAX%];Primary Key Object Pointer
Global camera_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type camera
	;Purpose: 
	;Properties:
	Field ID%;
	Field vpivot%;
	Field pitch#;
	Field maxpitch#;
	Field minpitch#;
	Field entity%;
	Field state%;
End Type

;============================
;METHODS
;============================
Function cameraStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
End Function

Function cameraStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.camera=Each camera
		cameraDelete(this)
	Next
End Function

Function cameraNew.camera(camerapitch#=0,cameramaxpitch#=90,cameraminpitch#=-90)
	;Purpose: CONSTRUCTOR - Creates camera Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: camera Instance
	this.camera=New camera
	this\ID%=0
	this\vpivot%=CreatePivot()
	this\pitch#=camerapitch#
	this\maxpitch#=cameramaxpitch#
	this\minpitch#=cameraminpitch#
	this\entity%=CreateCamera(this\vpivot%)
	this\state%=0
	hideentity(this\entity%)
	;CameraProjMode(this\entity%,this\state%)
	camera_Index=camera_Index+1
	this\ID%=camera_Index
	camera_ID[this\ID%]=this
	Return this
End Function

Function cameraDelete(this.camera)
	;Purpose: DESTRUCTOR - Removes camera Instance
	;Parameters: camera Instance
	;Return: None
	FreeEntity(this\entity%)
	FreeEntity(this\vpivot%)
	this\minpitch#=0.0
	this\maxpitch#=0.0
	this\pitch#=0.0
	this\state%=0
	Delete this
End Function

Function cameraUpdate()
	;Purpose: Main Loop Update. Updates all camera Instances.
	;Parameters: None
	;Return: None
	For this.camera=Each camera
		If this\pitch#>this\maxpitch# this\pitch#=this\maxpitch#
		If this\pitch#<this\minpitch# this\pitch#=this\minpitch#
		RotateEntity(this\entity%,this\pitch#,0,0)
	Next
End Function

Function cameraWrite(this.camera,camerafile)
	;Purpose: Writes camera Property Values to File
	;Parameters: camera Instance, camerafile=filehandle
	;Return: None
	WriteInt(camerafile,this\ID%)
	WriteInt(camerafile,this\vpivot%)
	WriteFloat(camerafile,this\pitch#)
	WriteFloat(camerafile,this\maxpitch#)
	WriteFloat(camerafile,this\minpitch#)
	WriteInt(camerafile,this\entity%)
End Function

Function cameraRead.camera(camerafile)
	;Purpose: Reads File To camera Property Values
	;Parameters: camerafile=filehandle
	;Return: camera Instance
	this.camera=New camera
	this\ID%=ReadInt(camerafile)
	this\vpivot%=ReadInt(camerafile)
	this\pitch#=ReadFloat(camerafile)
	this\maxpitch#=ReadFloat(camerafile)
	this\minpitch#=ReadFloat(camerafile)
	this\entity%=ReadInt(camerafile)
	camera_ID[this\ID%]=this
	Return this
End Function

Function cameraSave(camerafilename$="camera.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: camerafilename$= Name of File
	;Return: None
	camerafile=WriteFile(camerafilename$)
	For this.camera= Each camera
		cameraWrite(this,camerafile)
	Next
	CloseFile(camerafile)
End Function

Function cameraOpen(camerafilename$="camera.dat")
	;Purpose: Opens a Object Property file
	;Parameters: camerafilename$=Name of Property File
	;Return: None
	camerafile=ReadFile(camerafilename$)
	Repeat
		cameraRead(camerafile)
	Until Eof(camerafile)
	CloseFile(camerafile)
End Function

Function cameraCopy.camera(this.camera)
	;Purpose: Creates a Copy of a camera Instance
	;Parameters: camera Instance
	;Return: Copy of camera Instance
	copy.camera=cameraNew()
	copy\vpivot%=this\vpivot%
	copy\pitch#=this\pitch#
	copy\maxpitch#=this\maxpitch#
	copy\minpitch#=this\minpitch#
	copy\entity%=this\entity%
	Return copy
End Function

Function cameraMimic(this.camera,mimic.camera)
	;Purpose: Copies Property Values from one camera Instance To another
	;Parameters: camera Instance
	;Return: None
	mimic\vpivot%=this\vpivot%
	mimic\pitch#=this\pitch#
	mimic\maxpitch#=this\maxpitch#
	mimic\minpitch#=this\minpitch#
	mimic\entity%=this\entity%
End Function

Function cameraCreate.camera(camerapitch#=0,cameramaxpitch#=90,cameraminpitch#=-90)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.camera=cameraNew()
	this\vpivot%=CreatePivot()
	this\pitch#=camerapitch#
	this\maxpitch#=cameramaxpitch#
	this\minpitch#=cameraminpitch#
	this\entity%=CreateCamera(this\vpivot%)
	Return this
End Function

Function cameraDestroy(cameraid%)
	;Purpose: Remove object by ID
	;Parameters: cameraid%=Object's ID
	;Return: None
	cameraDelete(camera_ID[cameraid%])
End Function

Function cameraSet(this.camera,cameravpivot%,camerapitch#,cameramaxpitch#,cameraminpitch#,cameraentity%)
	;Purpose: Set camera Property Values
	;Parameters: camera Instance and Properties
	;Return: None
	this\vpivot%=cameravpivot%
	this\pitch#=camerapitch#
	this\maxpitch#=cameramaxpitch#
	this\minpitch#=cameraminpitch#
	this\entity%=cameraentity%
	EntityParent(this\entity%,this\vpivot%)
End Function

Function cameraSetState(this.camera,camerastate%=0)
	;Purpose: Sets camera state
	;Parameters: camera Instance and Properties
	;Return: None
	this\state%=camerastate%
	Select This\State
		case 1 Showentity(this\entity%)
		case 0 hideentity(this\entity%)
	end select
	;CameraProjMode(this\entity%,this\state%)
End Function

Function cameraMethod.camera(this.camera)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
