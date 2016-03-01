;============================
;WEAPON CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================
;Include "..\reactor\recycler.bb"

;============================
;CONSTANTS
;============================
Const WEAPON_MAX%=16
Const WEAPON_TRIGGER_RELEASEFIRE=1
Const WEAPON_TRIGGER_HITFIRE=2
Const WEAPON_TRIGGER_RPULSEFIRE=3
Const WEAPON_TRIGGER_RELEASEFIRE_HITDETONATE=4
Const WEAPON_TRIGGER_HITFIRE_HITDETONATE=5

;============================
;GLOBALS
;============================
Global weapon_ID.weapon[WEAPON_MAX%];Primary Key Object Pointer
Global weapon_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type weapon
	;Purpose: Profiles for various weapons.
	;Properties:
	Field ID%;
	Field projectileID%;
	Field chargeupaudioID%;
	Field coolingdownaudioID%;
	Field emptyaudioID%
	Field reloadaudioID% 
	Field recyclerID%; 
	Field copies%;
	Field name$;	
	Field filename$;
	Field entity%;
	Field hudimagefilename$
	Field hudimage%
	Field hudcursorfilename$
	Field hudcursor%
	Field hudoffsetx#;
	Field hudoffsety#;
	Field hudoffsetz#;	
	Field projectiles%;
	Field chargeup#;
	Field recoil#;
	Field cooling#;
	Field trigger%;
	Field triggerdelay#;
	Field reloadspeed#;
	Field chargeupfactor#;
	Field chargedownfactor#;
	Field coolingfactor#;
	Field coolingdownfactor#;	
End Type

;============================
;METHODS
;============================
Function weaponStart(weaponfilename$="weapon.dat")
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	weaponOpen(weaponfilename$)
	For this.weapon= Each weapon
		this\entity%=LoadAnimMesh(this\filename$)
		HideEntity(this\entity%)
		;create recycler and store object key
		this\recyclerID%=recyclerCreate(this\copies%)
		;create recycler entity copies for stack push/pop 
		recyclerEntityCopy(recycler_ID[this\recyclerID%],this\entity%,this\copies%)
		this\hudcursor%=LoadAnimTexture(this\hudcursorfilename$,4,64,64,0,1)
	Next
End Function

Function weaponStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.weapon=Each weapon
		weaponDelete(this)
	Next
End Function

Function weaponNew.weapon()
	;Purpose: CONSTRUCTOR - Creates weapon Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: weapon Instance
	this.weapon=New weapon
	this\ID%=0
	this\projectileID%=0
	this\chargeupaudioID%=0
	this\coolingdownaudioID%=0
	this\emptyaudioID%=0
	this\reloadaudioID%=0
	this\recyclerID%=0
	this\copies%=0
	this\name$=""	
	this\filename$=""
	this\entity%=0
	this\hudimagefilename$=""
	this\hudimage%=0
	this\hudcursorfilename$=""
	this\hudcursor%=0
	this\hudoffsetz#=0.0
	this\hudoffsety#=0.0
	this\hudoffsetx#=0.0	
	this\projectiles%=0
	this\chargeup#=0.0
	this\recoil#=0.0
	this\cooling#=0.0
	this\trigger%=0
	this\triggerdelay#=0.0
	this\reloadspeed#=0.0
	this\chargeupfactor#=0.0
	this\chargedownfactor#=0.0
	this\coolingfactor#=0.0
	this\coolingdownfactor#=0.0	
	weapon_Index=weapon_Index+1
	this\id%=weapon_Index
	weapon_ID[this\id%]=this
	Return this
End Function

Function weaponDelete(this.weapon)
	;Purpose: DESTRUCTOR - Removes weapon Instance
	;Parameters: weapon Instance
	;Return: None
	FreeEntity this\entity%
	this\filename$=""
	Delete this
End Function

Function weaponUpdate()
	;Purpose: Main Loop Update. Updates all weapon Instances.
	;Parameters: None
	;Return: None
	For this.weapon=Each weapon
		;Do stuff to 'this' here!
	Next
End Function

Function weaponWrite(this.weapon,weaponfile)
	;Purpose: Writes weapon Property Values to File
	;Parameters: weapon Instance, weaponfile=filehandle
	;Return: None
	WriteInt(weaponfile,this\ID%)
	WriteInt(weaponfile,this\projectileID%)
	WriteInt(weaponfile,this\chargeupaudioID%)
	WriteInt(weaponfile,this\coolingdownaudioID%)
	WriteInt(weaponfile,this\emptyaudioID%)
	WriteInt(weaponfile,this\reloadaudioID%)
	WriteInt(weaponfile,this\recyclerID%)
	WriteInt(weaponfile,this\copies%)	
	WriteString(weaponfile,this\name$)	
	WriteString(weaponfile,this\filename$)
	WriteInt(weaponfile,this\entity%)
	WriteString(weaponfile,this\hudimagefilename$)
	WriteInt(weaponfile,this\hudimage%)
	WriteString(weaponfile,this\hudcursorfilename$)
	WriteInt(weaponfile,this\hudcursor%)	
	WriteFloat(weaponfile,this\hudoffsetx#)
	WriteFloat(weaponfile,this\hudoffsety#)
	WriteFloat(weaponfile,this\hudoffsetz#)	
	WriteInt(weaponfile,this\projectiles%)
	WriteFloat(weaponfile,this\chargeup#)
	WriteFloat(weaponfile,this\recoil#)
	WriteFloat(weaponfile,this\cooling#)
	WriteInt(weaponfile,this\trigger%)
	WriteFloat(weaponfile,this\triggerdelay#)
	WriteFloat(weaponfile,this\reloadspeed#)
	WriteFloat(weaponfile,this\chargeupfactor#)
	WriteFloat(weaponfile,this\chargedownfactor#)
	WriteFloat(weaponfile,this\coolingfactor#)
	WriteFloat(weaponfile,this\coolingdownfactor#)	
End Function

Function weaponRead.weapon(weaponfile)
	;Purpose: Reads File To weapon Property Values
	;Parameters: weaponfile=filehandle
	;Return: weapon Instance
	this.weapon=New weapon
	this\ID%=ReadInt(weaponfile)
	this\projectileID%=ReadInt(weaponfile)
	this\chargeupaudioID%=ReadInt(weaponfile)
	this\coolingdownaudioID%=ReadInt(weaponfile)
	this\emptyaudioID%=readint(weaponfile)
	this\reloadaudioID%=readint(weaponfile)
	this\recyclerID%=ReadInt(weaponfile)
	this\copies%=ReadInt(weaponfile)	
	this\name$=ReadString(weaponfile)
	this\filename$=ReadString(weaponfile)
	this\entity%=ReadInt(weaponfile)
	this\hudimagefilename$=ReadString(weaponfile)
	this\hudimage%=ReadInt(weaponfile)
	this\hudcursorfilename$=ReadString(weaponfile)
	this\hudcursor%=ReadInt(weaponfile)
	this\hudoffsetx#=ReadFloat(weaponfile)
	this\hudoffsety#=ReadFloat(weaponfile)
	this\hudoffsetz#=ReadFloat(weaponfile)	
	this\projectiles%=ReadInt(weaponfile)
	this\chargeup#=ReadFloat(weaponfile)
	this\recoil#=ReadFloat(weaponfile)
	this\cooling#=ReadFloat(weaponfile)
	this\trigger%=ReadInt(weaponfile)
	this\triggerdelay#=ReadFloat(weaponfile)
	this\reloadspeed#=ReadFloat(weaponfile)
	this\chargeupfactor#=ReadFloat(weaponfile)
	this\chargedownfactor#=ReadFloat(weaponfile)
	this\coolingfactor#=ReadFloat(weaponfile)
	this\coolingdownfactor#=ReadFloat(weaponfile)	
	weapon_ID[this\id%]=this
	Return this
End Function

Function weaponSave(weaponfilename$="weapon.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: weaponfilename$= Name of File
	;Return: None
	weaponfile=WriteFile(weaponfilename$)
	For this.weapon= Each weapon
		weaponWrite(this,weaponfile)
	Next
	CloseFile(weaponfile)
End Function

Function weaponOpen(weaponfilename$="weapon.dat")
	;Purpose: Opens a Object Property file
	;Parameters: weaponfilename$=Name of Property File
	;Return: None
	weaponfile=ReadFile(weaponfilename$)
	Repeat
		weaponRead(weaponfile)
	Until Eof(weaponfile)
	CloseFile(weaponfile)
End Function

Function weaponCopy.weapon(this.weapon)
	;Purpose: Creates a Copy of a weapon Instance
	;Parameters: weapon Instance
	;Return: Copy of weapon Instance
	copy.weapon=weaponNew()
	copy\ID%=this\ID%
	copy\projectileID%=this\projectileID%
	copy\chargeupaudioID%=this\chargeupaudioID%
	copy\coolingdownaudioID%=this\coolingdownaudioID%
	copy\emptyaudioID%=this\emptyaudioID%
	copy\reloadaudioID%=this\reloadaudioID%	
	copy\recyclerID%=this\recyclerID%
	copy\copies%=this\copies%	
	copy\name$=this\name$	
	copy\filename$=this\filename$
	copy\entity%=this\entity%
	copy\hudimagefilename$=this\hudimagefilename$
	copy\hudimage%=this\hudimage%
	copy\hudcursorfilename$=this\hudcursorfilename$
	copy\hudcursor%=this\hudcursor%	
	copy\hudoffsetx#=this\hudoffsetx#
	copy\hudoffsety#=this\hudoffsety#
	copy\hudoffsetz#=this\hudoffsetz#	
	copy\projectiles%=this\projectiles%
	copy\chargeup#=this\chargeup#
	copy\recoil#=this\recoil#
	copy\cooling#=this\cooling#
	copy\trigger%=this\trigger%
	copy\triggerdelay#=this\triggerdelay#
	copy\reloadspeed#=this\reloadspeed#
	copy\chargeupfactor#=this\chargeupfactor#
	copy\chargedownfactor#=this\chargedownfactor#
	copy\coolingfactor#=this\coolingfactor#
	copy\coolingdownfactor#=this\coolingdownfactor#	
	Return copy
End Function

Function weaponMimic(this.weapon,mimic.weapon)
	;Purpose: Copies Property Values from one weapon Instance To another
	;Parameters: weapon Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\projectileID%=this\projectileID%
	mimic\chargeupaudioID%=this\chargeupaudioID%
	mimic\coolingdownaudioID%=this\coolingdownaudioID%
	mimic\emptyaudioID%=this\emptyaudioID%
	mimic\reloadaudioID%=this\reloadaudioID%	
	mimic\recyclerID%=this\recyclerID%
	mimic\copies%=this\copies%
	mimic\name$=this\name$	
	mimic\filename$=this\filename$
	mimic\entity%=this\entity%
	mimic\hudimagefilename$=this\hudimagefilename$
	mimic\hudimage%=this\hudimage%
	mimic\hudcursorfilename$=this\hudcursorfilename$
	mimic\hudcursor%=this\hudcursor%	
	mimic\hudoffsetx#=this\hudoffsetx#
	mimic\hudoffsety#=this\hudoffsety#
	mimic\hudoffsetz#=this\hudoffsetz#	
	mimic\projectiles%=this\projectiles%
	mimic\chargeup#=this\chargeup#
	mimic\recoil#=this\recoil#
	mimic\cooling#=this\cooling#
	mimic\trigger%=this\trigger%
	mimic\triggerdelay#=this\triggerdelay#
	mimic\reloadspeed#=this\reloadspeed#
	mimic\chargeupfactor#=this\chargeupfactor#
	mimic\chargedownfactor#=this\chargedownfactor#
	mimic\coolingfactor#=this\coolingfactor#
	mimic\coolingdownfactor#=this\coolingdownfactor#	
End Function

Function weaponCreate%(weaponID%,weaponprojectileID%,weaponchargeupaudioID%,weaponrecyclerID%,weaponcopies%,weaponfilename$,weaponentity%,weaponprojectiles%,weaponhudoffsetx#,weaponhudoffsety#,weaponhudoffsetz#,weaponchargeup#,weaponrecoil#,weaponcooling#,weapontrigger%,weapontriggerdelay#,weaponreloadspeed#)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.weapon=weaponNew()
	this\ID%=weaponID%
	this\projectileID%=weaponprojectileID%
	this\chargeupaudioID%=weaponchargeupaudioID%
	this\recyclerID%=weaponrecyclerID%
	this\copies%=weaponcopies%
	this\name$=weaponname$	
	this\filename$=weaponfilename$
	this\entity%=weaponentity%
	this\hudimagefilename$=weaponhudimagefilename$
	this\hudimage%=weaponhudimage%
	this\hudcursorfilename$=weaponhudcursorfilename$
	this\hudcursor%=weaponhudcursor%	
	this\hudoffsetx#=weaponhudoffsetx#
	this\hudoffsety#=weaponhudoffsety#
	this\hudoffsetz#=weaponhudoffsetz#	
	this\projectiles%=weaponprojectiles%
	this\chargeup#=weaponchargeup#
	this\recoil#=weaponrecoil#
	this\cooling#=weaponcooling#
	this\trigger%=weapontrigger%
	this\triggerdelay#=weapontriggerdelay#
	this\reloadspeed#=weaponreloadspeed#
	Return this\id
End Function

Function weaponDestroy(weaponid%)
	;Purpose: Remove object by ID
	;Parameters: weaponid%=Object's ID
	;Return: None
	weaponDelete(weapon_ID[weaponid%])
End Function

Function weaponSet(this.weapon,weaponID%,weaponprojectileID%,weaponchargeupaudioID%,weaponrecyclerID%,weaponcopies%,weaponfilename$,weaponentity%,weaponprojectiles%,weaponhudoffsetx#,weaponhudoffsety#,weaponhudoffsetz#,weaponchargeup#,weaponrecoil#,weaponcooling#,weapontrigger%,weapontriggerdelay#,weaponreloadspeed#)
	;Purpose: Set weapon Property Values
	;Parameters: weapon Instance and Properties
	;Return: None
	this\ID%=weaponID%
	this\projectileID%=weaponprojectileID%
	this\chargeupaudioID%=weaponchargeupaudioID%
	this\recyclerID%=weaponrecyclerID%
	this\copies%=weaponcopies%
	this\name$=weaponname$	
	this\filename$=weaponfilename$
	this\entity%=weaponentity%
	this\hudimagefilename$=weaponhudimagefilename$
	this\hudimage%=weaponhudimage%
	this\hudcursorfilename$=weaponhudcursorfilename$
	this\hudcursor%=weaponhudcursor%	
	this\projectiles%=weaponprojectiles%
	this\chargeup#=weaponchargeup#
	this\recoil#=weaponrecoil#
	this\cooling#=weaponcooling#
	this\trigger%=weapontrigger%
	this\triggerdelay#=weapontriggerdelay#
	this\reloadspeed#=weaponreloadspeed#
End Function

Function weaponDefaultDatWrite()
	;Purpose: Creates default weapon dat file
	;Parameters: TBD
	;Return: weapon Instance

	;plasmagun
	this.weapon=weaponNew()
	this\projectileID%=1
	this\chargeupaudioID%=9
	this\coolingdownaudioID%=3
	this\emptyaudioID%=4
	this\reloadaudioID%=5
	this\copies=32	
	this\name$="Plasma Shifter"	
	this\filename$="..\game\data\3d\weapons\plasmagun.b3d"
	this\hudimagefilename$=""
	this\hudcursorfilename$="..\game\data\3d\weapons\plasmaguncursor.png"
	this\hudoffsetx#=1.02571;1.4
	this\hudoffsety#=-1.0178;-1.0
	this\hudoffsetz#=1.17622;1.6		
	this\projectiles%=100
	this\chargeup#=2.0
	this\recoil#=0.25
	this\cooling#=2500.0
	this\trigger%=WEAPON_TRIGGER_HITFIRE
	this\triggerdelay#=0.10
	this\reloadspeed#=24
	this\chargeupfactor#=2.0
	this\chargedownfactor#=1.0
	this\coolingfactor#=50.0
	this\coolingdownfactor#=25.0

	;plasmarifle
	this.weapon=weaponNew()
	this\projectileID%=2
	this\chargeupaudioID%=9
	this\coolingdownaudioID%=3
	this\emptyaudioID%=4
	this\reloadaudioID%=5
	this\copies=32	
	this\name$="Red Shocker"	
	this\filename$="..\game\data\3d\weapons\plasmagun2.b3d"
	this\hudimagefilename$=""
	this\hudcursorfilename$="..\game\data\3d\weapons\plasmaguncursor2.png"
	this\hudoffsetx#=1.02455
	this\hudoffsety#=-1.01695
	this\hudoffsetz#=1.37358	
	this\projectiles%=50
	this\chargeup#=48.0
	this\recoil#=0.1
	this\cooling#=5000.0
	this\trigger%=WEAPON_TRIGGER_HITFIRE 
	this\triggerdelay#=0.10
	this\reloadspeed#=48
	this\chargeupfactor#=2.0
	this\chargedownfactor#=1.0
	this\coolingfactor#=50.0
	this\coolingdownfactor#=25.0

	;plasma grenade launcher
	this.weapon=weaponNew()
	this\projectileID%=3
	this\chargeupaudioID%=9
	this\coolingdownaudioID%=3
	this\emptyaudioID%=4
	this\reloadaudioID%=5
	this\copies=32	
	this\name$="Grenade Launcher"	
	this\filename$="..\game\data\3d\weapons\plasmagun3.b3d"
	this\hudimagefilename$=""
	this\hudcursorfilename$="..\game\data\3d\weapons\plasmaguncursor2.png"
	this\hudoffsetx#=1.02455
	this\hudoffsety#=-1.01695
	this\hudoffsetz#=1.37358	
	this\projectiles%=50
	this\chargeup#=48.0
	this\recoil#=0.1
	this\cooling#=5000.0
	this\trigger%=WEAPON_TRIGGER_RELEASEFIRE 
	this\triggerdelay#=0.10
	this\reloadspeed#=48
	this\chargeupfactor#=2.0
	this\chargedownfactor#=1.0
	this\coolingfactor#=50.0
	this\coolingdownfactor#=25.0
	
	weaponSave()
End Function

Function weaponMethod.weapon(this.weapon)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

If gamedatawrite weaponDefaultDatWrite