;============================
;SPAWNER CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const SPAWNER_MAX%=64
Const SPAWNER_HEALTH%=111
Const SPAWNER_AMMO%=222

;============================
;GLOBALS
;============================
Global SPAWNER_STATE_IDLE%=0
Global SPAWNER_STATE_ACTION%=1

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type spawner
	;Purpose: 
	;Properties:
	Field itemID%;
	Field entity%;
	Field respawnCTR%;
	Field state%;
	Field x#, y#, z#
End Type

;============================
;METHODS
;============================
Function spawnerStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	spawnerDefaultDatWrite()
	this.spawner=spawnerRead("spawnerdefault.dat")
End Function

Function spawnerStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.spawner=Each spawner
		spawnerDelete(this)
	Next
End Function

Function spawnerNew.spawner()
	;Purpose: CONSTRUCTOR - Creates spawner Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: spawner Instance
	this.spawner=New spawner
	this\itemID%=0
	this\entity%=0
	this\respawnCTR%=0
	this\state%=0
	this\x#=0
	this\y#=0
	this\z#=0
	Return this
End Function

Function spawnerDelete(this.spawner)
	;Purpose: DESTRUCTOR - Removes spawner Instance
	;Parameters: spawner Instance
	;Return: None
	FreeEntity this\entity%
	Delete this
End Function

Function spawnerUpdate()
	;Purpose: Main Loop Update. Updates all spawner Instances.
	;Parameters: None
	;Return: None
	For this.spawner=Each spawner
		item.item=item_ID[this\itemID%]
		
		For cycle%=1 To PLAYER_MAX%
			If EntityDistance(this\entity%, player_ID[cycle%]\entity%)<2
				this\state%=SPAWNER_STATE_ACTIVE
			Else
				If this\state%=SPAWNER_STATE_SPAWNING
					this\state%=SPAWNER_STATE_SPAWNING
				Else
					this\state%=SPAWNER_STATE_IDLE
				EndIf 
			EndIf 
		
			Select this\state%
				Case SPAWNER_STATE_IDLE
					TurnEntity this\entity%, 0, 0.5, 0
				Case SPAWNER_STATE_ACTIVE
					TurnEntity this\entity%, 0, 0.5, 0
					If KeyHit(33) ;key "F" - pickup key
						this\state%=SPAWNER_STATE_SPAWNING
						Select this\itemID%
							Case SPAWNER_HEALTH%
								;player_ID[cycle%]\health%=100
							Case SPAWNER_AMMO%
								;add ammo to gun
						End Select
					EndIf  
				Case SPAWNER_STATE_SPAWNING
					;timer
					this\state%=SPAWNER_STATE_IDLE				
			End Select 
				
		Next		
		
	Next
End Function 

Function spawnerWrite(this.spawner,spawnerfile)
	;Purpose: Writes spawner Property Values to File
	;Parameters: spawner Instance, spawnerfile=filehandle
	;Return: None
	WriteInt(spawnerfile,this\itemID%)
	WriteInt(spawnerfile,this\entity%)
	WriteInt(spawnerfile,this\respawnCTR%)
	WriteInt(spawnerfile,this\state%)
	WriteFloat(spawnerfile,this\x#)
	WriteFloat(spawnerfile,this\y#)
	WriteFloat(spawnerfile,this\z#)
End Function

Function spawnerRead.spawner(spawnerfile)
	;Purpose: Reads File To spawner Property Values
	;Parameters: spawnerfile=filehandle
	;Return: spawner Instance
	this.spawner=New spawner
	this\itemID%=ReadInt(spawnerfile)
	this\entity%=ReadInt(spawnerfile)
	this\respawnCTR%=ReadInt(spawnerfile)
	this\state%=ReadInt(spawnerfile)
	this\x#=ReadFloat(spawnerfile)
	this\y#=ReadFloat(spawnerfile)
	this\z#=ReadFloat(spawnerfile)
	Return this
End Function

Function spawnerSave(spawnerfilename$="spawner.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: spawnerfilename$= Name of File
	;Return: None
	spawnerfile=WriteFile(spawnerfilename$)
	For this.spawner= Each spawner
		spawnerWrite(this,spawnerfile)
	Next
	CloseFile(spawnerfile)
End Function

Function spawnerOpen(spawnerfilename$="spawner.dat")
	;Purpose: Opens a Object Property file
	;Parameters: spawnerfilename$=Name of Property File
	;Return: None
	spawnerfile=ReadFile(spawnerfilename$)
	Repeat
		spawnerRead(spawnerfile)
	Until Eof(spawnerfile)
	CloseFile(spawnerfile)
End Function

Function spawnerCopy.spawner(this.spawner)
	;Purpose: Creates a Copy of a spawner Instance
	;Parameters: spawner Instance
	;Return: Copy of spawner Instance
	copy.spawner=spawnerNew()
	copy\itemID%=this\itemID%
	copy\entity%=this\entity%
	copy\respawnCTR%=this\respawnCTR%
	copy\state%=this\state%
	copy\x#=this\x#
	copy\y#=this\y#
	copy\z#=this\z#
	Return copy
End Function

Function spawnerMimic(this.spawner,mimic.spawner)
	;Purpose: Copies Property Values from one spawner Instance To another
	;Parameters: spawner Instance
	;Return: None
	mimic\itemID%=this\itemID%
	mimic\entity%=this\entity%
	mimic\respawnCTR%=this\respawnCTR%
	mimic\state%=this\state%
	mimic\x#=this\x#
	mimic\y#=this\y#
	mimic\z#=this\z#
End Function

Function spawnerSet(this.spawner,spawneritemID%,spawnerentity%,spawnerrespawnCTR%,spawnerstate%, spawnerx#, spawnery#, spawnerz#)
	;Purpose: Set spawner Property Values
	;Parameters: spawner Instance and Properties
	;Return: None
	this\itemID%=spawneritemID%
	this\entity%=spawnerentity%
	this\respawnCTR%=spawnerrespawnCTR%
	this\state%=spawnerstate%
	this\x#=spawnerx#
	this\y#=spawnery#
	this\z#=spawnerz#
End Function

Function spawnerMethod.spawner(this.spawner)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

Function spawnerDefaultDatWrite()
	;Purpose: to write default values for testing
	;Parameters: None
	;Returns: Nothing
	this.spawner=spawnerNew()
	this\itemID%=HEALTH%
	this\entity%=item_ID[this\itemID%]\entity%
	this\respawnCTR%=0
	this\state%=SPAWNER_STATE_IDLE%
	this\x#=-10
	this\y#=0
	this\z#=10
	spawnerWrite(this.spawner, "defaultspawner.dat")
End Function