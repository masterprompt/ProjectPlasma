;============================
;CONTROLMAP CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const CONTROLMAP_MAX=16
Const CONTROLMAP_PRESS_DOWN=0
Const CONTROLMAP_PRESS_HIT=1

;============================
;GLOBALS
;============================
Global controlmap_ID.controlmap[CONTROLMAP_MAX%];Primary Key Object Pointer
Global controlmap_Index%;Primary Key ID Management Counter
Global controlmap_enabled%;Disabler for typing

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type controlmap
	;Purpose: 
	;Properties:
	Field ID%;
	Field mouselookx%;
	Field mouselooky%;
	Field mousezoom%;
	Field forward%;
	Field backward%;
	Field strafeleft%;
	Field straferight%;
	Field jump%;
	Field duck%;
	Field gunselect1%;
	Field gunselect2%
	Field gununmount1%
	Field gununmount2%
	Field gunfire1%;
	Field gunfire2%;
	Field gunzoom%;
	Field networkmenu%;
	Field teammenu%;
	Field help%;
	Field chat%;
	Field chatteam%;
	Field chatall%;
	Field screenshot%;	
	Field escape%
End Type

;============================
;METHODS
;============================
Function controlmapStart(controlmapfilename$="controlmap.dat")
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	controlmapopen(controlmapfilename$)
End Function

Function controlmapStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.controlmap=Each controlmap
		controlmapDelete(this)
	Next
End Function

Function controlmapNew.controlmap()
	;Purpose: CONSTRUCTOR - Creates controlmap Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: controlmap Instance
	this.controlmap=New controlmap
	this\ID%=0
	this\mouselookx%=244;mx
	this\mouselooky%=243 ;my
	this\mousezoom%=245;mz
	this\forward%=17;w
	this\backward%=31;s
	this\strafeleft%=30;a
	this\straferight%=32;d
	this\jump%=57;space
	this\duck%=56;
	this\gunselect1%=29;lctrl
	this\gunselect2%=157;rctrl
	this\gununmount1%=56;lalt
	this\gununmount2%=184;ralt
	this\gunfire1%=238;mlb
	this\gunfire2%=239;mrb
	this\gunzoom%=240;
	this\networkmenu%=61;f3
	this\teammenu%=60;f2
	this\help%=59;f1
	this\chat%=2;1
	this\chatteam%=3;2
	this\chatall%=4;3
	this\screenshot%=63;f9	
	this\escape=1;escape	
	controlmap_Index=controlmap_Index+1
	this\id%=controlmap_Index
	controlmap_ID[this\id%]=this
	Return this
End Function

Function controlmapDelete(this.controlmap)
	;Purpose: DESTRUCTOR - Removes controlmap Instance
	;Parameters: controlmap Instance
	;Return: None
	Delete this
End Function

Function controlmapUpdate()
	;Purpose: Main Loop Update. Updates all controlmap Instances.
	;Parameters: None
	;Return: None
	if controlmap_enabled% then MoveMouse(game\screenwidth/2,game\screenwidth/2);center mouse on screen
	For this.controlmap=Each controlmap
		;Do stuff to 'this' here!
	Next
End Function

Function controlmapWrite(this.controlmap,controlmapfile)
	;Purpose: Writes controlmap Property Values to File
	;Parameters: controlmap Instance, controlmapfile=filehandle
	;Return: None
	WriteInt(controlmapfile,this\ID%)
	WriteInt(controlmapfile,this\mouselookx%)
	WriteInt(controlmapfile,this\mouselooky%)
	WriteInt(controlmapfile,this\mousezoom%)
	WriteInt(controlmapfile,this\forward%)
	WriteInt(controlmapfile,this\backward%)
	WriteInt(controlmapfile,this\strafeleft%)
	WriteInt(controlmapfile,this\straferight%)
	WriteInt(controlmapfile,this\jump%)
	WriteInt(controlmapfile,this\duck%)
	WriteInt(controlmapfile,this\gunselect1%)
	WriteInt(controlmapfile,this\gunselect2%)	
	WriteInt(controlmapfile,this\gununmount1%)
	WriteInt(controlmapfile,this\gununmount2%)		
	WriteInt(controlmapfile,this\gunfire1%)
	WriteInt(controlmapfile,this\gunfire2%)
	WriteInt(controlmapfile,this\gunzoom%)
	WriteInt(controlmapfile,this\networkmenu%)
	WriteInt(controlmapfile,this\teammenu%)
	WriteInt(controlmapfile,this\help%)
	WriteInt(controlmapfile,this\chat%)
	WriteInt(controlmapfile,this\chatteam%)
	WriteInt(controlmapfile,this\chatall%)
	WriteInt(controlmapfile,this\screenshot%)
	WriteInt(controlmapfile,this\escape%)	
End Function

Function controlmapRead.controlmap(controlmapfile)
	;Purpose: Reads File To controlmap Property Values
	;Parameters: controlmapfile=filehandle
	;Return: controlmap Instance
	this.controlmap=New controlmap
	this\ID%=ReadInt(controlmapfile)
	this\mouselookx%=ReadInt(controlmapfile)
	this\mouselooky%=ReadInt(controlmapfile)
	this\mousezoom%=ReadInt(controlmapfile)
	this\forward%=ReadInt(controlmapfile)
	this\backward%=ReadInt(controlmapfile)
	this\strafeleft%=ReadInt(controlmapfile)
	this\straferight%=ReadInt(controlmapfile)
	this\jump%=ReadInt(controlmapfile)
	this\duck%=ReadInt(controlmapfile)
	this\gunselect1%=ReadInt(controlmapfile)
	this\gunselect2%=ReadInt(controlmapfile)
	this\gununmount1%=ReadInt(controlmapfile)
	this\gununmount2%=ReadInt(controlmapfile)	
	this\gunfire1%=ReadInt(controlmapfile)
	this\gunfire2%=ReadInt(controlmapfile)
	this\gunzoom%=ReadInt(controlmapfile)
	this\networkmenu%=ReadInt(controlmapfile)
	this\teammenu%=ReadInt(controlmapfile)
	this\help%=ReadInt(controlmapfile)
	this\chat%=ReadInt(controlmapfile)
	this\chatteam%=ReadInt(controlmapfile)
	this\chatall%=ReadInt(controlmapfile)
	this\screenshot%=ReadInt(controlmapfile)
	this\escape%=ReadInt(controlmapfile)	
	controlmap_ID[this\id%]=this
	Return this
End Function

Function controlmapSave(controlmapfilename$="controlmap.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: controlmapfilename$= Name of File
	;Return: None
	controlmapfile=WriteFile(controlmapfilename$)
	For this.controlmap= Each controlmap
		controlmapWrite(this,controlmapfile)
	Next
	CloseFile(controlmapfile)
End Function

Function controlmapOpen(controlmapfilename$="controlmap.dat")
	;Purpose: Opens a Object Property file
	;Parameters: controlmapfilename$=Name of Property File
	;Return: None
	controlmapfile=ReadFile(controlmapfilename$)
	Repeat
		controlmapRead(controlmapfile)
	Until Eof(controlmapfile)
	CloseFile(controlmapfile)
End Function

Function controlmapCopy.controlmap(this.controlmap)
	;Purpose: Creates a Copy of a controlmap Instance
	;Parameters: controlmap Instance
	;Return: Copy of controlmap Instance
	copy.controlmap=controlmapNew()
	copy\ID%=this\ID%
	copy\mouselookx%=this\mouselookx%
	copy\mouselooky%=this\mouselooky%
	copy\mousezoom%=this\mousezoom%
	copy\forward%=this\forward%
	copy\backward%=this\backward%
	copy\strafeleft%=this\strafeleft%
	copy\straferight%=this\straferight%
	copy\jump%=this\jump%
	copy\duck%=this\duck%
	copy\gunselect1%=this\gunselect1%
	copy\gunselect2%=this\gunselect2%	
	copy\gununmount1%=this\gununmount1%
	copy\gununmount2%=this\gununmount2%	
	copy\gunfire1%=this\gunfire1%
	copy\gunfire2%=this\gunfire2%
	copy\gunzoom%=this\gunzoom%
	copy\networkmenu%=this\networkmenu%
	copy\teammenu%=this\teammenu%
	copy\help%=this\help%
	copy\chat%=this\chat%
	copy\chatteam%=this\chatteam%
	copy\chatall%=this\chatall%
	copy\screenshot%=this\screenshot%
	copy\escape%=this\escape%	
	Return copy
End Function

Function controlmapMimic(this.controlmap,mimic.controlmap)
	;Purpose: Copies Property Values from one controlmap Instance To another
	;Parameters: controlmap Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\mouselookx%=this\mouselookx%
	mimic\mouselooky%=this\mouselooky%
	mimic\mousezoom%=this\mousezoom%
	mimic\forward%=this\forward%
	mimic\backward%=this\backward%
	mimic\strafeleft%=this\strafeleft%
	mimic\straferight%=this\straferight%
	mimic\jump%=this\jump%
	mimic\duck%=this\duck%
	mimic\gunselect1%=this\gunselect1%
	mimic\gunselect2%=this\gunselect2%	
	mimic\gununmount1%=this\gununmount1%
	mimic\gununmount2%=this\gununmount2%		
	mimic\gunfire1%=this\gunfire1%
	mimic\gunfire2%=this\gunfire2%
	mimic\gunzoom%=this\gunzoom%
	mimic\networkmenu%=this\networkmenu%
	mimic\teammenu%=this\teammenu%
	mimic\help%=this\help%
	mimic\chat%=this\chat%
	mimic\chatteam%=this\chatteam%
	mimic\chatall%=this\chatall%
	mimic\screenshot%=this\screenshot%
	mimic\escape%=this\escape%	
End Function

Function controlmapCreate%(controlmapID%,controlmapmouselookx%,controlmapmouselooky%,controlmapmousezoom%,controlmapforward%,controlmapbackward%,controlmapstrafeleft%,controlmapstraferight%,controlmapjump%,controlmapduck%,controlmapgunselect%,controlmapgunfire1%,controlmapgunzoom%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.controlmap=controlmapNew()
	this\ID%=controlmapID%
	this\mouselookx%=controlmapmouselookx%
	this\mouselooky%=controlmapmouselooky%
	this\mousezoom%=controlmapmousezoom%
	this\forward%=controlmapforward%
	this\backward%=controlmapbackward%
	this\strafeleft%=controlmapstrafeleft%
	this\straferight%=controlmapstraferight%
	this\jump%=controlmapjump%
	this\duck%=controlmapduck%
	this\gunselect1%=controlmapgunselect1%
	this\gunselect2%=controlmapgunselect2%
	this\gununmount1%=controlmapgununmount1%
	this\gununmount2%=controlmapgununmount2%	
	this\gunfire1%=controlmapgunfire1%
	this\gunfire2%=controlmapgunfire2%
	this\gunzoom%=controlmapgunzoom%
	Return this\id
End Function

Function controlmapDestroy(controlmapid%)
	;Purpose: Remove object by ID
	;Parameters: controlmapid%=Object's ID
	;Return: None
	controlmapDelete(controlmap_ID[controlmapid%])
End Function

Function controlmapSet(this.controlmap,controlmapID%,controlmapmouselookx%,controlmapmouselooky%,controlmapmousezoom%,controlmapforward%,controlmapbackward%,controlmapstrafeleft%,controlmapstraferight%,controlmapjump%,controlmapduck%,controlmapgunselect%,controlmapgunfire1%,controlmapgunzoom%)
	;Purpose: Set controlmap Property Values
	;Parameters: controlmap Instance and Properties
	;Return: None
	this\ID%=controlmapID%
	this\mouselookx%=controlmapmouselookx%
	this\mouselooky%=controlmapmouselooky%
	this\mousezoom%=controlmapmousezoom%
	this\forward%=controlmapforward%
	this\backward%=controlmapbackward%
	this\strafeleft%=controlmapstrafeleft%
	this\straferight%=controlmapstraferight%
	this\jump%=controlmapjump%
	this\duck%=controlmapduck%
	this\gunselect1%=controlmapgunselect1%
	this\gunselect2%=controlmapgunselect2%	
	this\gununmount1%=controlmapgununmount1%
	this\gununmount2%=controlmapgununmount2%
	this\gunfire1%=controlmapgunfire1%
	this\gunfire2%=controlmapgunfire2%	
	this\gunzoom%=controlmapgunzoom%
End Function


Function controlmapGetInput(controlmapkey,controlmappress=CONTROLMAP_PRESS_DOWN)
	;Purpose: Returns input based on key value
	;Parameters: Desired Input Refference
	;	0-237 keyboard
	;	238-242 mousebutton
	;	243-245 mousepointer
	;Return: Key/Mouse Value
	if controlmap_enabled% then
		Select controlmapkey
			Case 243 Return(MouseXSpeed())
			Case 244 Return(MouseYSpeed())
			Case 245 Return(MouseZSpeed())
			;Case 253 Return(JoyPitch())
			;Case 254 Return(JoyYaw())
			;Case 255 Return(JoyRoll())
			Default
				If controlmappress
					If controlmapkey < 238 And controlmapkey > 0  Return(KeyHit(controlmapkey))
					If controlmapkey >=238 And controlmapkey <=242 Return(MouseHit(controlmapkey-237))
					;if controlmapkey >=246 And controlmapkey <=250 Return(JoyHit(controlmapkey-245))
				Else 
					If controlmapkey < 238 And controlmapkey > 0 Return(KeyDown(controlmapkey))
					If controlmapkey >=238 And controlmapkey <=242 Return(MouseDown(controlmapkey-237))
					;if controlmapkey >=246 And controlmapkey <=250 Return(JoyHit(controlmapkey-245))		
				EndIf			
		End Select
	else
		return(0)
	endif
End Function 

Function controlmapDefaultDatWrite()
	;Purpose: Set Default Control Map
	;Parameters: Controlmap Instance
	;Return: none
	this.controlmap=controlmapNew()
	controlmapSave()
End Function

Function controlmapMethod.controlmap(this.controlmap)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

If gamedatawrite controlmapDefaultDatWrite

;Revision Notes
;Add: New fields gunselect and gununmount for dual weapon.
;Add: Const CONTROL_PRESS_HIT,CONTROL_PRESS_DOWN;
;Modify: All ingame dynamic fields relocated to other classes for efficiency.
;Modify: Expanded controlmapGetInput() to support Hit,Down, and prep for joy. 
