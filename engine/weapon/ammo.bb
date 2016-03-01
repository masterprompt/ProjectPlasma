;============================
;AMMO CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const AMMO_MAX%=256
Const AMMO_STATE_DEAD=0
Const AMMO_STATE_LOADING=1
Const AMMO_STATE_FIRING=2
Const AMMO_STATE_TRAVELING=3
Const AMMO_STATE_IMPACTING=4
Const AMMO_STATE_EFFECTING=5

;============================
;GLOBALS
;============================
Global ammo_stackPTR%; Pointer for ammo_stack array

;============================
;ARRAYS
;============================
Dim ammo_stack.ammo(AMMO_MAX%)

;============================
;OBJECT
;============================
Type ammo
	;Purpose: A projectile fired from a gun.
	;Properties:
	Field projectileID%;
	Field entity%;
	Field rangeCTR#;
	Field effectdecayCTR#;
	Field traveltarget%;
	Field travelangle#
	Field physics.physics ;object pointer
	Field state%;
End Type

;============================
;METHODS
;============================
Function ammoStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	;create a ammo objects to be recycled during runtime
	;	faster than create/delete during runtime
	For ammoloop=1 To AMMO_MAX
		ammoStackPush(ammoNew())	
	Next
End Function

Function ammoStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.ammo=Each ammo
		ammoDelete(this)
	Next
End Function

Function ammoNew.ammo()
	;Purpose: CONSTRUCTOR - Creates ammo Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: ammo Instance
	this.ammo=New ammo
	this\projectileID%=0
	this\entity%=0
	this\rangeCTR#=0.0
	this\effectdecayCTR#=0.0
	this\traveltarget%=0
	this\state%=0
	Return this
End Function

Function ammoDelete(this.ammo)
	;Purpose: DESTRUCTOR - Removes ammo Instance
	;Parameters: ammo Instance
	;Return: None
	Delete this
End Function

Function ammoUpdate()
	;Purpose: Main Loop Update. Updates all ammo Instances.
	;Parameters: None
	;Return: None
	For this.ammo=Each ammo
		If this\state% <> AMMO_STATE_DEAD
			projectile.projectile=projectile_ID[this\projectileID%]
			;project state check
			Select this\state%
				Case AMMO_STATE_LOADING
					;generate empty cell particle
					particle.particle=particleStackPop();generate particle projectile
					particleSet(particle,projectile\loadreactorID%,recyclerPop(recycler_ID[reactor_ID[projectile\loadreactorID%]\recyclerID%]),PARTICLE_STATE_ALIVE)
				Case AMMO_STATE_FIRING
					;generate flash particle
					particle.particle=particleStackPop();generate particle projectile
					particleSet(particle,projectile\firereactorID%,recyclerPop(recycler_ID[reactor_ID[projectile\firereactorID%]\recyclerID%]),PARTICLE_STATE_ALIVE)
					PositionEntity(particle\entity,EntityX(this\entity),EntityY(this\entity),EntityZ(this\entity))
					EntityType(this\entity,GAME_COLLISIONTYPE_PROJECTILE)
					;generate fire sound	
					sound.sound=soundStackPop()
					soundSet(sound,projectile\fireaudioID,recyclerAudioPop(recycler_ID[audio_ID[projectile\fireaudioID]\recyclerID%]),this\rangeCTR,SOUND_STATE_PLAYING)
					EmitSound(sound\channel,this\entity)
					;generate traveling sound
					;sound.sound=soundStackPop()
					;soundSet(sound,1,recyclerAudioPop(recycler_ID[audio_ID[1]\recyclerID%]),SOUND_STATE_STOP)
					;EmitSound(sound\channel,this\entity)
					;bind physics entity
					If projectile\traveltype=PROJECTILE_TRAVELTYPE_LOB
						this\physics.physics=physicsStackPop()
						physicsSet(this\physics,this\entity,projectile\radius#,projectile\travelspeed#,projectile\bounce#,projectile\friction#,projectile\force#)
						;physics testing
						;For physicsloop=1 To markerCTR
						;	FreeEntity(marker(physicsloop))
						;Next
						;markerCTR=0
					EndIf
					this\state%=AMMO_STATE_TRAVELING
				Case AMMO_STATE_TRAVELING
					;decrement travel/lifetime
					this\rangeCTR#=this\rangeCTR#-1
					If this\rangeCTR<=0 this\state%=AMMO_STATE_IMPACTING	
					;move ammo based on travel type; lob is handled by physics
					Select projectile\traveltype
						Case PROJECTILE_TRAVELTYPE_STRAIGHT
							;ammo rotation
							Select projectile\entitytype
								Case PROJECTILE_ENTITYTYPE_MESH TurnEntity(this\entity,projectile\travelpitchfactor#,projectile\travelyawfactor#,projectile\travelrollfactor#)
								Case PROJECTILE_ENTITYTYPE_SPRITE, PROJECTILE_ENTITYTYPE_ANIMSPRITE
									this\travelangle#=this\travelangle#+projectile\travelanglefactor#
									RotateSprite(this\entity,this\travelangle#)
							End Select						
							MoveEntity(this\entity,0,0,projectile\travelspeed#)
						Case  PROJECTILE_TRAVELTYPE_LOB ;handled by physics object
					End Select	
					particle.particle=particleStackPop();generate particle projectile
					particleSet(particle,projectile\travelreactorID%,recyclerPop(recycler_ID[reactor_ID[projectile\travelreactorID%]\recyclerID%]),PARTICLE_STATE_ALIVE)
					PositionEntity(particle\entity,EntityX(this\entity),EntityY(this\entity),EntityZ(this\entity))

					If EntityCollided(this\entity,GAME_COLLISIONTYPE_LEVEL) 
						Select  projectile\detonatetype%
							Case PROJECTILE_DETONATETYPE_IMPACT
								;play impact wall sound
								For ammocountcollision=1 To CountCollisions(this\entity )
									If GetEntityType(CollisionEntity(this\entity,ammocountcollision))=GAME_COLLISIONTYPE_LEVEL
										ammocx#=CollisionX(this\entity,ammocountcollision)
										ammocy#=CollisionY(this\entity,ammocountcollision)
										ammocz#=CollisionZ(this\entity,ammocountcollision)
										ammonx#=CollisionNX(this\entity,ammocountcollision)
										ammony#=CollisionNY(this\entity,ammocountcollision)
										ammonz#=CollisionNZ(this\entity,ammocountcollision)
										particle.particle=particleStackPop()
										particleSet(particle,projectile\debrisreactorID%,recyclerPop(recycler_ID[reactor_ID[projectile\debrisreactorID%]\recyclerID%]),PARTICLE_STATE_ALIVE)
										PositionEntity(particle\entity,ammocx,ammocy,ammocz)
										SpriteViewMode(particle\entity,2)
										AlignToVector(particle\entity,-ammonx,-ammony,-ammonz,3)
										MoveEntity(particle\entity,0,0,-.1)
										Exit
									EndIf
								Next
								this\state%=AMMO_STATE_IMPACTING
							;Case PROJECTILE_DETONATETYPE_LOB
							;	this\rangeCTR#=this\rangeCTR#-1
							;	If this\rangeCTR<=0 this\state%=AMMO_STATE_IMPACTING								
						End Select
					ElseIf EntityCollided(this\entity,GAME_COLLISIONTYPE_TERRAIN)
						;play impact ground sound
						If  projectile\detonatetype%=PROJECTILE_DETONATETYPE_IMPACT this\state%=AMMO_STATE_IMPACTING
					ElseIf EntityCollided(this\entity,GAME_COLLISIONTYPE_PLAYER)
						PLAYERHIT=PLAYERHIT+1
						DEBUGLOG "PLAYERHIT="+PLAYERHIT
						this\state%=AMMO_STATE_IMPACTING
					ElseIf EntityCollided(this\entity,GAME_COLLISIONTYPE_PROP)
						;play impact propsound
						If  projectile\detonatetype%=PROJECTILE_DETONATETYPE_IMPACT this\state%=AMMO_STATE_IMPACTING						
					;Else
					;	this\rangeCTR#=this\rangeCTR#-1
					;	If this\rangeCTR<=0 this\state%=AMMO_STATE_IMPACTING
					EndIf							
				Case AMMO_STATE_IMPACTING	
					;generate explosion particle
					particle.particle=particleStackPop();generate particle projectile
					particleSet(particle,projectile\impactreactorID%,recyclerPop(recycler_ID[reactor_ID[projectile\impactreactorID%]\recyclerID%]),PARTICLE_STATE_ALIVE)
					PositionEntity(particle\entity,EntityX(this\entity),EntityY(this\entity),EntityZ(this\entity))
					;generate impact sound
					sound.sound=soundStackPop()
					soundSet(sound,projectile\impactaudioID,recyclerAudioPop(recycler_ID[audio_ID[projectile\impactaudioID]\recyclerID%]),particle\lifeCTR,SOUND_STATE_PLAYING)
					EmitSound(sound\channel,particle\entity)
					;generate effect
					ammoEffect(this)
					EntityType(this\entity,GAME_COLLISIONTYPE_NONE)
					recyclerPush(recycler_ID[projectile\recyclerID%],this\entity)
					ammoStackPush(this)
					If projectile\traveltype=PROJECTILE_TRAVELTYPE_LOB physicsStackPush(this\physics)
					this\state%=AMMO_STATE_DEAD
			End Select
		EndIf
	Next
End Function

Function ammoWrite(this.ammo,ammofile)
	;Purpose: Writes ammo Property Values to File
	;Parameters: ammo Instance, ammofile=filehandle
	;Return: None
	WriteInt(ammofile,this\projectileID%)
	WriteInt(ammofile,this\entity%)
	WriteFloat(ammofile,this\rangeCTR#)
	WriteFloat(ammofile,this\effectdecayCTR#)
	WriteInt(ammofile,this\traveltarget%)
	WriteInt(ammofile,this\state%)
End Function

Function ammoRead.ammo(ammofile)
	;Purpose: Reads File To ammo Property Values
	;Parameters: ammofile=filehandle
	;Return: ammo Instance
	this.ammo=New ammo
	this\projectileID%=ReadInt(ammofile)
	this\entity%=ReadInt(ammofile)
	this\rangeCTR#=ReadFloat(ammofile)
	this\effectdecayCTR#=ReadFloat(ammofile)
	this\traveltarget%=ReadInt(ammofile)
	this\state%=ReadInt(ammofile)
	Return this
End Function

Function ammoSave(ammofilename$="ammo.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: ammofilename$= Name of File
	;Return: None
	ammofile=WriteFile(ammofilename$)
	For this.ammo= Each ammo
		ammoWrite(this,ammofile)
	Next
	CloseFile(ammofile)
End Function

Function ammoOpen(ammofilename$="ammo.dat")
	;Purpose: Opens a Object Property file
	;Parameters: ammofilename$=Name of Property File
	;Return: None
	ammofile=ReadFile(ammofilename$)
	Repeat
		ammoRead(ammofile)
	Until Eof(ammofile)
	CloseFile(ammofile)
End Function

Function ammoCopy.ammo(this.ammo)
	;Purpose: Creates a Copy of a ammo Instance
	;Parameters: ammo Instance
	;Return: Copy of ammo Instance
	copy.ammo=ammoNew()
	copy\projectileID%=this\projectileID%
	copy\entity%=this\entity%
	copy\rangeCTR#=this\rangeCTR#
	copy\effectdecayCTR#=this\effectdecayCTR#
	copy\traveltarget%=this\traveltarget%
	copy\state%=this\state%
	Return copy
End Function

Function ammoMimic(this.ammo,mimic.ammo)
	;Purpose: Copies Property Values from one ammo Instance To another
	;Parameters: ammo Instance
	;Return: None
	mimic\projectileID%=this\projectileID%
	mimic\entity%=this\entity%
	mimic\rangeCTR#=this\rangeCTR#
	mimic\effectdecayCTR#=this\effectdecayCTR#
	mimic\traveltarget%=this\traveltarget%
	mimic\state%=this\state%
End Function

Function ammoSet(this.ammo,ammoprojectileID%,ammoentity%,ammotraveltarget%,ammostate%)
	;Purpose: Set ammo Property Values
	;Parameters: ammo Instance and Properties
	;Return: None
	this\projectileID%=ammoprojectileID%
	projectile.projectile=projectile_ID[this\projectileID%]
	this\entity%=ammoentity%
	this\rangeCTR#=projectile\range#
	this\effectdecayCTR#=projectile\effectdecay#
	this\traveltarget%=ammotraveltarget%
	this\state%=ammostate%
End Function

Function ammoStackPush(this.ammo)
	;Purpose: Pushes ammo object into ammo_stack
	;Parameters: TBD
	;Return: TBD
	ammo_stack(ammo_stackPTR%)=this
	ammo_stackPTR%=ammo_stackPTR%+1	
End Function

Function ammoStackPop.ammo()
	;Purpose: Pops ammo object out of ammo_stack
	;Parameters: TBD
	;Return: TBD
	ammo_stackPTR%=ammo_stackPTR%-1	
	Return ammo_stack(ammo_stackPTR%)	
End Function

Function ammoEffect(this.ammo)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	projectile.projectile=projectile_ID[this\projectileID%]
	;projectile\effect%
	;projectile\effecttarget%
	;projectile\effectduration%
	;projectile\effectarea%
	;projectile\effectvalue%
	;projectile\effectdecay#
End Function

Function ammoMethod.ammo(this.ammo)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function