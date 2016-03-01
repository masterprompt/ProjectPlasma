;============================
;GUN CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const GUN_MAX%=16
Const GUN_STATE_IDLE%=0
Const GUN_STATE_CHARGING%=1
Const GUN_STATE_CHARGED%=2
Const GUN_STATE_FIRING%=3
Const GUN_STATE_COOLING%=4
Const GUN_STATE_RELOAD%=5
Const GUN_STATE_RELOADING%=6
Const GUN_STATE_EMPTY%=7
Const GUN_TRIGGER_STATE_IDLE%=0
Const GUN_TRIGGER_STATE_DOWN%=1
Const GUN_TRIGGER_STATE_UP%=2
Const GUN_GRIP_RIGHT=1
Const GUN_GRIP_LEFT=-1

;============================
;GLOBALS
;============================
Global gun_stackPTR%; Pointer for gun_stack array

;============================
;ARRAYS
;============================
Dim gun_stack.gun(GUN_MAX%)

;============================
;OBJECT
;============================
Type gun
	;Purpose: A handheld weapon that fires ammo projectiles long distances.
	;Properties:
	Field weaponID%;
	Field entity%;
	Field flashpivot%
	Field grippivot%[2]
	Field meterpivot%
	Field lockedtarget%; dynamic target lock (entity handle?)
	Field projectilesCTR%;
	Field chargeupCTR#;
	Field recoilCTR#;
	Field coolingCTR#;
	Field triggerdelayCTR#;
	Field reloadspeedCTR#;
	field triggerstate%
	field triggeroldstate%
	Field state%;
End Type

;============================
;METHODS
;============================
Function gunStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
End Function

Function gunStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.gun=Each gun
		gunDelete(this)
	Next
End Function

Function gunNew.gun()
	;Purpose: CONSTRUCTOR - Creates gun Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: gun Instance
	this.gun=New gun
	this\weaponID%=0
	this\entity%=0
	this\projectilesCTR%=0
	this\chargeupCTR#=0.0
	this\recoilCTR#=0.0
	this\coolingCTR#=0.0
	this\triggerdelayCTR#=0.0
	this\reloadspeedCTR#=0.0
	this\state%=0
	Return this
End Function

Function gunDelete(this.gun)
	;Purpose: DESTRUCTOR - Removes gun Instance
	;Parameters: gun Instance
	;Return: None
	this\reloadspeedCTR#=0.0
	this\triggerdelayCTR#=0.0
	this\coolingCTR#=0.0
	this\recoilCTR#=0.0
	this\chargeupCTR#=0.0
	Freeentity this\entity%
	Delete this
End Function

Function gunUpdate()
	;Purpose: Main Loop Update. Updates all gun Instances.
	;Parameters: None
	;Return: None
	For this.gun=Each gun
		weapon.weapon=weapon_ID[this\weaponID%]
		;TRIGGER IDLE (0,0)																
		If this\triggerstate=GUN_TRIGGER_STATE_IDLE And this\triggeroldstate=GUN_TRIGGER_STATE_IDLE																
			Select this\state%															
				Case GUN_STATE_IDLE%														
					If this\coolingCTR>0 this\coolingCTR=this\coolingCTR-1													
				Case GUN_STATE_CHARGING%														
					;play chargedown sound													
					If this\coolingCTR>0 this\coolingCTR=this\coolingCTR-1													
					this\chargeupCTR#=this\chargeupCTR#-weapon\chargedownfactor#													
					If this\chargeupCTR<0 													
						this\chargeupCTR=0												
						this\state=GUN_STATE_IDLE%												
					EndIf													
				Case GUN_STATE_FIRING%														
					;fire weapon on release													
					;play fire sound													
					;pop ammo object off ammostack; assign projectileID; pop entity off projectile recycler; set ammo properties by projectile template													
					ammo.ammo=ammoStackPop();generate ammo projectile													
					ammoSet(ammo,weapon\projectileID%,RecyclerPop(recycler_ID[projectile_ID[weapon\projectileID%]\recyclerID%]),this\lockedtarget,AMMO_STATE_FIRING)													
					;orientate ammo projectile: align to weapon, position at ammo pivot													
					PositionEntity(ammo\entity%,EntityX(this\flashpivot,True),EntityY(this\flashpivot,True),EntityZ(this\flashpivot,True))													
					RotateEntity(ammo\entity,EntityPitch(this\entity,True),EntityYaw(this\entity,True),Rnd(360.0));player_SightPivot%													
					;reset reloading counter													
					this\projectilesCTR=this\projectilesCTR%-1													
					this\state%=GUN_STATE_IDLE%													
					this\coolingCTR#=this\coolingCTR#+weapon\coolingfactor#													
					If this\coolingCTR#>weapon\cooling#													
						sound.sound=soundStackPop()												
						soundSet(sound,weapon\coolingdownaudioID,recyclerAudioPop(recycler_ID[audio_ID[weapon\coolingdownaudioID]\recyclerID%]),24,SOUND_STATE_PLAYING)												
						EmitSound(sound\channel,this\entity)												
						this\state%=GUN_STATE_COOLING%												
					endif													
					;generate loading particle													
					;if this\projectiles 													
					;	particle.particle=particleStackPop();generate particle projectile												
					;	particleSet(particle,projectile\impactreactorID%,recyclerPop(recycler_ID[reactor_ID[projectile\impactreactorID%]\recyclerID%]),PARTICLE_STATE_ALIVE)												
					;endif													
				Case GUN_STATE_COOLING%														
					this\coolingCTR#=this\coolingCTR#-weapon\coolingdownfactor#													
					If  this\coolingCTR<0 this\state%=GUN_STATE_IDLE%													
				Case GUN_STATE_RELOAD%														
					;play reload sound													
					sound.sound=soundStackPop()													
					soundSet(sound,weapon\reloadaudioID,recyclerAudioPop(recycler_ID[audio_ID[weapon\reloadaudioID]\recyclerID%]),24,SOUND_STATE_PLAYING)													
					EmitSound(sound\channel,this\entity)													
					;play reload animation													
					this\reloadspeedCTR=weapon\reloadspeed													
					this\state=GUN_STATE_RELOADING%													
				Case GUN_STATE_RELOADING%														
					this\reloadspeedCTR=this\reloadspeedCTR-1													
					If not this\reloadspeedCTR													
						this\projectilesCTR=weapon\projectiles%												
						this\state=GUN_STATE_IDLE%												
					EndIf													
				End Select														
		;TRIGGER HIT (1,0)																
		ElseIf this\triggerstate=GUN_TRIGGER_STATE_DOWN And this\triggeroldstate=GUN_TRIGGER_STATE_IDLE																
			Select this\state%															
				Case GUN_STATE_IDLE%														
					If this\coolingCTR>0 this\coolingCTR=this\coolingCTR-1													
					If  this\projectilesCTR=0													
						;play empty clip sound												
						sound.sound=soundStackPop()												
						soundSet(sound,weapon\emptyaudioID,recyclerAudioPop(recycler_ID[audio_ID[weapon\emptyaudioID]\recyclerID%]),1,SOUND_STATE_PLAYING)												
						EmitSound(sound\channel,this\entity)												
					Else													
						;chargeup sound												
						sound.sound=soundStackPop()												
						soundSet(sound,weapon\chargeupaudioID,recyclerAudioPop(recycler_ID[audio_ID[weapon\chargeupaudioID]\recyclerID%]),24,SOUND_STATE_PLAYING)												
						EmitSound(sound\channel,this\entity)												
						this\state%=GUN_STATE_CHARGING%												
					EndIf													
			End Select															
		;TRIGGER HOLD (1,1)																
		ElseIf this\triggerstate=GUN_TRIGGER_STATE_DOWN And this\triggeroldstate=GUN_TRIGGER_STATE_DOWN																
			Select this\state															
				Case GUN_STATE_IDLE%														
					If this\coolingCTR>0 this\coolingCTR=this\coolingCTR-1													
					If  this\projectilesCTR<0													
						;play empty clip sound												
						sound.sound=soundStackPop()												
						soundSet(sound,weapon\emptyaudioID,recyclerAudioPop(recycler_ID[audio_ID[weapon\emptyaudioID]\recyclerID%]),1,SOUND_STATE_PLAYING)												
						EmitSound(sound\channel,this\entity)												
					EndIf													
				Case GUN_STATE_CHARGING														
					If this\coolingCTR>0 this\coolingCTR=this\coolingCTR-1													
					;play chargeup sound													
					this\chargeupCTR%=this\chargeupCTR%+weapon\chargeupfactor#													
					If this\chargeupCTR%=>weapon\chargeup% this\state=GUN_STATE_CHARGED%													
				Case GUN_STATE_CHARGED%														
					this\coolingCTR#=this\coolingCTR#-1													
					If this\coolingCTR#<0 this\coolingCTR#=0													
					;reset charge													
					this\chargeupCTR%=0													
					If weapon\trigger<>WEAPON_TRIGGER_RELEASEFIRE this\state=GUN_STATE_FIRING% ;fire weapon on hold and charged.													
				Case GUN_STATE_FIRING														
					;pop ammo object off ammostack; assign projectileID; pop entity off projectile recycler; set ammo properties by projectile template													
					ammo.ammo=ammoStackPop();generate ammo projectile													
					ammoSet(ammo,weapon\projectileID%,RecyclerPop(recycler_ID[projectile_ID[weapon\projectileID%]\recyclerID%]),this\lockedtarget,AMMO_STATE_FIRING)													
					;orientate ammo projectile: align to weapon, position at ammo pivot													
					PositionEntity(ammo\entity%,EntityX(this\flashpivot,True),EntityY(this\flashpivot,True),EntityZ(this\flashpivot,True))													
					RotateEntity(ammo\entity,EntityPitch(this\entity,True),EntityYaw(this\entity,True),EntityRoll(this\entity,True));player_SightPivot%													
					;reset reloading counter													
					this\projectilesCTR=this\projectilesCTR%-1													
					this\state%=GUN_STATE_IDLE% ;single shot													
					 ;rpulse aka rapid fire													
					If weapon\trigger=WEAPON_TRIGGER_RPULSEFIRE this\state%=GUN_STATE_CHARGING													
					this\coolingCTR#=this\coolingCTR#+weapon\coolingfactor#													
					If this\coolingCTR#>weapon\cooling#													
						sound.sound=soundStackPop()												
						soundSet(sound,weapon\coolingdownaudioID,recyclerAudioPop(recycler_ID[audio_ID[weapon\coolingdownaudioID]\recyclerID%]),24,SOUND_STATE_PLAYING)												
						EmitSound(sound\channel,this\entity)												
						this\state%=GUN_STATE_COOLING%												
					endif													
				Case GUN_STATE_COOLING%														
					;play cooling sound													
					this\coolingCTR#=this\coolingCTR#-weapon\coolingdownfactor#													
					If this\coolingCTR#<0 this\state%=GUN_STATE_IDLE%													
			End Select															
		;TRIGGER RELEASE (0,1)																
		ElseIf this\triggerstate=GUN_TRIGGER_STATE_IDLE And this\triggeroldstate=GUN_TRIGGER_STATE_DOWN																
			Select this\state%															
				Case GUN_STATE_CHARGING														
					;play chargedown sound													
					If this\coolingCTR>0 this\coolingCTR=this\coolingCTR-1													
					this\chargeupCTR#=this\chargeupCTR#-weapon\chargedownfactor#													
					If this\chargeupCTR<0 													
						this\chargeupCTR=0												
						this\state=GUN_STATE_IDLE%												
					EndIF													
				Case GUN_STATE_CHARGED%														
					;reset charge													
					this\chargeupCTR%=0													
					this\state=GUN_STATE_FIRING%													
			End Select															
		EndIf
		this\triggeroldstate=this\triggerstate
		this\triggerstate=GUN_TRIGGER_STATE_IDLE
	Next
End Function

Function gunWrite(this.gun,gunfile)
	;Purpose: Writes gun Property Values to File
	;Parameters: gun Instance, gunfile=filehandle
	;Return: None
	WriteInt(gunfile,this\weaponID%)
	WriteInt(gunfile,this\entity%)
	WriteInt(gunfile,this\projectilesCTR%)
	WriteFloat(gunfile,this\chargeupCTR#)
	WriteFloat(gunfile,this\recoilCTR#)
	WriteFloat(gunfile,this\coolingCTR#)
	WriteFloat(gunfile,this\triggerdelayCTR#)
	WriteFloat(gunfile,this\reloadspeedCTR#)
	WriteInt(gunfile,this\state%)
End Function

Function gunRead.gun(gunfile)
	;Purpose: Reads File To gun Property Values
	;Parameters: gunfile=filehandle
	;Return: gun Instance
	this.gun=New gun
	this\weaponID%=ReadInt(gunfile)
	this\entity%=ReadInt(gunfile)
	this\projectilesCTR%=ReadInt(gunfile)
	this\chargeupCTR#=ReadFloat(gunfile)
	this\recoilCTR#=ReadFloat(gunfile)
	this\coolingCTR#=ReadFloat(gunfile)
	this\triggerdelayCTR#=ReadFloat(gunfile)
	this\reloadspeedCTR#=ReadFloat(gunfile)
	this\state%=ReadInt(gunfile)
	Return this
End Function

Function gunSave(gunfilename$="gun.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: gunfilename$= Name of File
	;Return: None
	gunfile=WriteFile(gunfilename$)
	For this.gun= Each gun
		gunWrite(this,gunfile)
	Next
	CloseFile(gunfile)
End Function

Function gunOpen(gunfilename$="gun.dat")
	;Purpose: Opens a Object Property file
	;Parameters: gunfilename$=Name of Property File
	;Return: None
	gunfile=ReadFile(gunfilename$)
	Repeat
		gunRead(gunfile)
	Until Eof(gunfile)
	CloseFile(gunfile)
End Function

Function gunCopy.gun(this.gun)
	;Purpose: Creates a Copy of a gun Instance
	;Parameters: gun Instance
	;Return: Copy of gun Instance
	copy.gun=gunNew()
	copy\weaponID%=this\weaponID%
	copy\entity%=this\entity%
	copy\projectilesCTR%=this\projectilesCTR%
	copy\chargeupCTR#=this\chargeupCTR#
	copy\recoilCTR#=this\recoilCTR#
	copy\coolingCTR#=this\coolingCTR#
	copy\triggerdelayCTR#=this\triggerdelayCTR#
	copy\reloadspeedCTR#=this\reloadspeedCTR#
	copy\state%=this\state%
	Return copy
End Function

Function gunMimic(this.gun,mimic.gun)
	;Purpose: Copies Property Values from one gun Instance To another
	;Parameters: gun Instance
	;Return: None
	mimic\weaponID%=this\weaponID%
	mimic\entity%=this\entity%
	mimic\projectilesCTR%=this\projectilesCTR%
	mimic\chargeupCTR#=this\chargeupCTR#
	mimic\recoilCTR#=this\recoilCTR#
	mimic\coolingCTR#=this\coolingCTR#
	mimic\triggerdelayCTR#=this\triggerdelayCTR#
	mimic\reloadspeedCTR#=this\reloadspeedCTR#
	mimic\state%=this\state%
End Function

Function gunSet(this.gun,gunweaponID%,gunentity%,gunstate%)
	;Purpose: Set gun Property Values
	;Parameters: gun Instance and Properties
	;Return: None
	this\weaponID%=gunweaponID%
	weapon.weapon=weapon_ID[this\weaponID%]
	this\entity%=gunentity%
	this\projectilesCTR%=weapon\projectiles%
	this\chargeupCTR#=weapon\chargeup#
	this\recoilCTR#=weapon\recoil#
	;this\coolingCTR#=weapon\cooling#
	this\triggerdelayCTR#=weapon\triggerdelay#
	this\reloadspeedCTR#=weapon\reloadspeed#
	this\state%=gunstate%
End Function

Function gunStackPush(this.gun)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	gun_stack(gun_stackPTR%)=this
	gun_stackPTR%=gun_stackPTR%+1	
End Function

Function gunStackPop.gun()
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	gun_stackPTR%=gun_stackPTR%-1	
	Return gun_stack(gun_stackPTR%)	
End Function

Function gunMount(this.gun,player.player,gungrip=GUN_GRIP_RIGHT)
	;Purpose: Mounts gun to player
	;Parameters: gun Instance
	;	player instance
	;	gungrip: left or right
	;Return: None
	;mount gun
	;get mounting pivots
	weapon.weapon=weapon_ID[this\weaponID%]
	projectile.projectile=projectile_ID[weapon\projectileID%]
	this\flashpivot=FindChild(this\entity,"flash01")
	this\grippivot[1]=FindChild(this\entity,"grip01")
	this\grippivot[2]=FindChild(this\entity,"grip02") ;gun\grippivot --> player\handpivot
	;setup sight
	EntityTexture(player_SightPivot%,weapon\hudcursor%)
	PositionEntity(player_SightPivot%,0,0,projectile\range#);adjust sight range by project range
	ScaleSprite(player_SightPivot%,projectile\range#/32,projectile\range#/32)
	;mount to camera	
	EntityParent(this\entity,player\camera\entity,False) 
	PositionEntity(this\entity,weapon\hudoffsetx#*gungrip,weapon\hudoffsety#,weapon\hudoffsetz#)
	PointEntity(this\entity,player_SightPivot%)
	;mount to player
	;If gungrip=GUN_GRIP_LEFT 
	;	EntityParent(gun\entity,player\grip02)
	;Else
	;	EntityParent(gun\entity,player\grip01)
	;End If
End Function

Function gunUnmount(this.gun)
	EntityParent(this\entity,0)
	recyclerPush(recycler_ID[weapon_ID[this\weaponID%]\recyclerID],this\entity)
	this\weaponID%=0
End Function

Function gunMethod.gun(this.gun)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function