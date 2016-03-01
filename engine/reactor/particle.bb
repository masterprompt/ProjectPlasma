;============================
;PARTICLE CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const PARTICLE_MAX%=256
Const PARTICLE_STATE_DEAD%=0
Const PARTICLE_STATE_ALIVE%=1
Const PARTICLE_STATE_DYING%=2

;============================
;GLOBALS
;============================
Global particle_stackPTR%; Pointer for particle_stack array

;============================
;ARRAYS
;============================
Dim particle_stack.particle(PARTICLE_MAX%)

;============================
;OBJECT
;============================
Type particle
	;Purpose: 
	;Properties:
	Field reactorID%;
	Field entity%;
	Field angle#
	Field xscale#;
	Field yscale#;
	Field zscale#;
	Field red#;
	Field green#;
	Field blue#;
	Field alpha#;	
	Field frame%;	
	Field lifeCTR%;
	Field state%;
End Type

;============================
;METHODS
;============================
Function particleStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For particleloop=1 To PARTICLE_MAX
		particleStackPush(particleNew())	
	Next	
End Function

Function particleStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.particle=Each particle
		particleDelete(this)
	Next
End Function

Function particleNew.particle()
	;Purpose: CONSTRUCTOR - Creates particle Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: particle Instance
	this.particle=New particle
	this\reactorID%=0
	this\entity%=0
	this\xscale#=0.0
	this\yscale#=0.0
	this\zscale#=0.0
	this\red#=0.0
	this\green#=0.0
	this\blue#=0.0
	this\alpha#=0.0	
	this\frame%=0	
	this\lifeCTR%=0.0
	this\state%=0
	Return this
End Function

Function particleDelete(this.particle)
	;Purpose: DESTRUCTOR - Removes particle Instance
	;Parameters: particle Instance
	;Return: None
	this\lifeCTR%=0.0
	;Freeentity this\entity%
	Delete this
End Function

Function particleUpdate()
	;Purpose: Main Loop Update. Updates all particle Instances.
	;Parameters: None
	;Return: None
	For this.particle=Each particle
		reactor.reactor=reactor_ID[this\reactorID]
		Select this\state%
			Case PARTICLE_STATE_DYING
					recyclerPush(recycler_ID[reactor\recyclerID%],this\entity)
					particleStackPush(this)
					this\state=PARTICLE_STATE_DEAD
			Case PARTICLE_STATE_ALIVE
				If reactor\effect/REACTOR_EFFECT_MOVE And True 
					MoveEntity(this\entity,reactor\xspeed1#,reactor\yspeed1#,reactor\zspeed1#)
				EndIf
				If reactor\effect/REACTOR_EFFECT_TURN And True 
					Select reactor\EntityType
						Case REACTOR_ENTITYTYPE_MESH
							TurnEntity(this\entity,EntityPitch(this\entity)+reactor\pitch1#,EntityYaw(this\entity)+reactor\yaw1#,EntityRoll(this\entity)+reactor\roll1#)
						Case REACTOR_ENTITYTYPE_SPRITE, REACTOR_ENTITYTYPE_ANIMSPRITE
							this\angle#=this\angle#+reactor\roll1#
							RotateSprite(this\entity,this\angle#)
					End Select
				EndIf
				If reactor\effect/REACTOR_EFFECT_SCALE And True
					Select reactor\EntityType
						Case REACTOR_ENTITYTYPE_MESH
							This\xscale#=this\xscale#+(Sgn(reactor\xscale2#-this\xscale#)*reactor\xscalefactor#) 
							this\yscale#=this\yscale#+(Sgn(reactor\yscale2#-this\yscale#)*reactor\yscalefactor#) 
							this\zscale#=this\zscale#+(Sgn(reactor\zscale2#-this\zscale#)*reactor\zscalefactor#) 
							ScaleEntity(this\entity,this\xscale#,this\yscale#,this\zscale#)
						Case REACTOR_ENTITYTYPE_SPRITE, REACTOR_ENTITYTYPE_ANIMSPRITE
							This\xscale#=this\xscale#+(Sgn(reactor\xscale2#-this\xscale#)*reactor\xscalefactor#) 
							this\yscale#=this\yscale#+(Sgn(reactor\yscale2#-this\yscale#)*reactor\yscalefactor#) 							
							ScaleSprite(this\entity,this\xscale#,this\yscale#)
					End Select	
				EndIf
				If reactor\effect/REACTOR_EFFECT_COLOR And True
					this\red#=this\red#+(Sgn(reactor\red2#-this\red#)*reactor\redfactor#) 
					this\green#=this\green#+(Sgn(reactor\green2#-this\green#)*reactor\greenfactor#) 
					this\blue#=this\blue#+(Sgn(reactor\blue2#-this\blue#)*reactor\bluefactor#) 
					EntityColor(this\entity,this\red#,this\green#,this\blue#)			
				EndIf
				If reactor\effect/REACTOR_EFFECT_ALPHA And True
					this\alpha#=this\alpha#+(Sgn(reactor\alpha2#-this\alpha#)*reactor\alphafactor#) 
					EntityAlpha(this\entity,this\alpha#)
				EndIf
				If reactor\effect/REACTOR_EFFECT_BLEND And True
				EndIf
				If reactor\effect/REACTOR_EFFECT_ANIMATE And True
					Select reactor\EntityType
						Case REACTOR_ENTITYTYPE_MESH
							SetAnimTime(this\entity,1.0,particleanimseq)
						Case REACTOR_ENTITYTYPE_ANIMSPRITE
							this\frame=this\frame+1
							EntityTexture(this\entity,reactor\texture%,this\frame) 
					End Select
				EndIf
				If this\lifeCTR>0 this\lifeCTR=this\lifeCTR-1
				If Not this\lifeCTR this\state=PARTICLE_STATE_DYING%
		End Select
	Next
End Function

Function particleWrite(this.particle,particlefile)
	;Purpose: Writes particle Property Values to File
	;Parameters: particle Instance, particlefile=filehandle
	;Return: None
	WriteInt(particlefile,this\reactorID%)
	WriteInt(particlefile,this\entity%)
	WriteFloat(particlefile,this\xscale#)
	WriteFloat(particlefile,this\yscale#)
	WriteFloat(particlefile,this\zscale#)
	WriteFloat(particlefile,this\red#)
	WriteFloat(particlefile,this\green#)
	WriteFloat(particlefile,this\blue#)
	WriteFloat(particlefile,this\alpha#)	
	WriteInt(particlefile,this\frame%)	
	WriteInt(particlefile,this\lifeCTR%)
	WriteInt(particlefile,this\state%)
End Function

Function particleRead.particle(particlefile)
	;Purpose: Reads File To particle Property Values
	;Parameters: particlefile=filehandle
	;Return: particle Instance
	this.particle=New particle
	this\reactorID%=ReadInt(particlefile)
	this\entity%=ReadInt(particlefile)
	this\xscale#=ReadFloat(particlefile)
	this\yscale#=ReadFloat(particlefile)
	this\zscale#=ReadFloat(particlefile)
	this\red#=ReadFloat(particlefile)
	this\green#=ReadFloat(particlefile)
	this\blue#=ReadFloat(particlefile)
	this\alpha#=ReadFloat(particlefile)	
	this\frame%=ReadInt(particlefile)	
	this\lifeCTR%=ReadInt(particlefile)
	this\state%=ReadInt(particlefile)
	Return this
End Function

Function particleSave(particlefilename$="particle.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: particlefilename$= Name of File
	;Return: None
	particlefile=WriteFile(particlefilename$)
	For this.particle= Each particle
		particleWrite(this,particlefile)
	Next
	CloseFile(particlefile)
End Function

Function particleOpen(particlefilename$="particle.dat")
	;Purpose: Opens a Object Property file
	;Parameters: particlefilename$=Name of Property File
	;Return: None
	particlefile=ReadFile(particlefilename$)
	Repeat
		particleRead(particlefile)
	Until Eof(particlefile)
	CloseFile(particlefile)
End Function

Function particleCopy.particle(this.particle)
	;Purpose: Creates a Copy of a particle Instance
	;Parameters: particle Instance
	;Return: Copy of particle Instance
	copy.particle=particleNew()
	copy\reactorID%=this\reactorID%
	copy\entity%=this\entity%
	copy\xscale#=this\xscale#
	copy\yscale#=this\yscale#
	copy\zscale#=this\zscale#
	copy\red#=this\red#
	copy\green#=this\green#
	copy\blue#=this\blue#
	copy\alpha#=this\alpha#	
	copy\frame%=this\frame%	
	copy\lifeCTR%=this\lifeCTR%
	copy\state%=this\state%
	Return copy
End Function

Function particleMimic(this.particle,mimic.particle)
	;Purpose: Copies Property Values from one particle Instance To another
	;Parameters: particle Instance
	;Return: None
	mimic\reactorID%=this\reactorID%
	mimic\entity%=this\entity%
	mimic\xscale#=this\xscale#
	mimic\yscale#=this\yscale#
	mimic\zscale#=this\zscale#
	mimic\red#=this\red#
	mimic\green#=this\green#
	mimic\blue#=this\blue#
	mimic\alpha#=this\alpha#
	mimic\frame%=this\frame%	
	mimic\lifeCTR%=this\lifeCTR%
	mimic\state%=this\state%
End Function

Function particleSet(this.particle,particlereactorID%,particleentity%,particlestate%)
	;Purpose: Set particle Property Values
	;Parameters: particle Instance and Properties
	;Return: None
	reactor.reactor=reactor_ID[particlereactorID%]
	this\reactorID%=particlereactorID%
	this\entity%=particleentity%
	this\xscale#=reactor\xscale1#
	this\yscale#=reactor\yscale1#
	this\zscale#=reactor\zscale1#
	this\red#=reactor\red1#
	this\green#=reactor\green1#
	this\blue#=reactor\blue1#
	this\alpha#=reactor\alpha1#	
	this\frame%=reactor\firstframe
	this\lifeCTR%=reactor\life%
	this\state%=particlestate%
	If reactor\light
		light.light=lightStackPop()
		Select reactor\lighttype
			Case LIGHT_TYPE_OMNI lightSet(light,recyclerOmniLightPop(recycler_ID[recycler_OmniLightID]),reactor\lighttype%,reactor\lightrange1#,reactor\lightinnerangle1#,reactor\lightouterangle1#,reactor\lightred1#,reactor\lightgreen1#,reactor\lightblue1#,reactor\life%,LIGHT_STATE_ON)
			Case LIGHT_TYPE_SPOT lightSet(light,recyclerSpotLightPop(recycler_ID[recycler_SpotLightID]),reactor\lighttype%,reactor\lightrange1#,reactor\lightinnerangle1#,reactor\lightouterangle1#,reactor\lightred1#,reactor\lightgreen1#,reactor\lightblue1#,reactor\life%,LIGHT_STATE_ON)
		End Select
		EntityParent(light\entity,this\entity,false)
	EndIf
End Function

Function particleStackPush(this.particle)
	;Purpose: Pushes particle object into particle_stack
	;Parameters: TBD
	;Return: TBD
	particle_stack(particle_stackPTR%)=this
	particle_stackPTR%=particle_stackPTR%+1	
End Function

Function particleStackPop.particle()
	;Purpose: Pops particle object out of particle_stack
	;Parameters: TBD
	;Return: TBD
	particle_stackPTR%=particle_stackPTR%-1	
	Return particle_stack(particle_stackPTR%)	
End Function

Function particleMethod.particle(this.particle)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function