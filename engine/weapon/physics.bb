;physics rolling physics example by Jeppe Nielsen 2003
;adapted by Frankie Taylor for TeamPLASMA

;============================
;PHYSICS CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const PHYSICS_MAX%=32
Const PHYSICS_GRAVITY#=-0.04

;============================
;GLOBALS
;============================
Global physics_Pivot ;global pivot shared by all physics entities
Global physics_stackPTR%

;============================
;ARRAYS
;============================
Dim physics_Stack.physics(PHYSICS_MAX%)

;============================
;OBJECT
;============================
Type physics
	;Purpose: 
	;Properties:
	Field pivot%;
	Field entity%;
	Field x#;position
	Field y#;
	Field z# ;
	Field xvel#;velocity
	Field yvel#;
	Field zvel# ;
	Field ax#;acceleration
	Field ay#;
	Field az# ;
	Field radius#;
	Field bounce#;bounce factor
	Field vel#;temp velocity
	Field xvel2#;
	Field yvel2#;
	Field zvel2#;
	Field friction#;friction factor
	Field force#;force factor
End Type

;============================
;METHODS
;============================
Function physicsStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	physics_Pivot=CreatePivot()
	For physicsloop=1 To PHYSICS_MAX
		this.physics=physicsNew()
		this\pivot=CreatePivot()
		physicsStackPush(this)	
	Next
End Function

Function physicsStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.physics=Each physics
		physicsDelete(this)
	Next
End Function

Function physicsNew.physics()
	;Purpose: CONSTRUCTOR - Creates physics Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: physics Instance
	this.physics=New physics
	this\pivot%=0
	this\entity%=0
	this\x#=0.0
	this\y#=0.0
	this\z# =0.0
	this\xvel#=0.0
	this\yvel#=0.0
	this\zvel# =0.0
	this\ax#=0.0
	this\ay#=0.0
	this\az# =0.0
	this\radius#=1.0
	this\bounce#=0.9
	this\vel#=0.0
	this\xvel2#=0.0
	this\yvel2#=0.0
	this\zvel2#=0.0
	this\friction#=0.98
	this\force#=-2.0
	Return this
End Function

Function physicsDelete(this.physics)
	;Purpose: DESTRUCTOR - Removes physics Instance
	;Parameters: physics Instance
	;Return: None
	this\force#=0.0
	this\friction#=0.0
	this\zvel2#=0.0
	this\yvel2#=0.0
	this\xvel2#=0.0
	this\vel#=0.0
	this\bounce#=0.0
	this\radius#=0.0
	this\az# =0.0
	this\ay#=0.0
	this\ax#=0.0
	this\zvel# =0.0
	this\yvel#=0.0
	this\xvel#=0.0
	this\z# =0.0
	this\y#=0.0
	this\x#=0.0
	FreeEntity this\entity%
	Delete this
End Function

Function physicsUpdate()
	;Purpose: Main Loop Update. Updates all physics Instances.
	;Parameters: None
	;Return: None
	For this.physics=Each physics
		if this\entity
			this\yvel#=this\yvel#+PHYSICS_GRAVITY#
			this\xvel#=this\xvel#+this\ax#
			this\yvel#=this\yvel#+this\ay#
			this\zvel#=this\zvel#+this\az#
			this\x#=EntityX(this\pivot)
			this\y#=EntityY(this\pivot)
			this\z#=EntityZ(this\pivot)
			TranslateEntity(this\pivot,this\xvel,this\yvel,this\zvel)
			;testing	
			;markerCTR=markerCTR+1
			;marker(markerCTR)=CopyEntity(marker(0))
			;PositionEntity(marker(markerCTR),EntityX(this\pivot),EntityY(this\pivot),EntityZ(this\pivot))
			;EntityColor(marker(markerCTR),255-(markerCTR*2),255-(markerCTR*2),255-(markerCTR*2))
			;If markerCTR=1 Then EntityColor(marker(markerCTR),0,255,0)
		EndIf
	Next
End Function

Function physicsUpdateWorld()
	;Purpose: RenderWorld Update for physics. Must be executed after updatwworld() in main loop.
	;	for proper collision response
	;Parameters: None
	;Return: None		
	For this.physics=Each physics
		If this\entity
			;correct velocity if collided
			this\xvel2=(EntityX(this\pivot)-this\x)
			this\yvel2=(EntityY(this\pivot)-this\y)
			this\zvel2=(EntityZ(this\pivot)-this\z)
			this\x#=EntityX(this\pivot)
			this\y#=EntityY(this\pivot)
			this\z#=EntityZ(this\pivot)
			PositionEntity(this\entity,this\x,this\y,this\z)
			PositionEntity(physics_Pivot,this\x,this\y,this\z)
			If EntityCollided(this\pivot,GAME_COLLISIONTYPE_LEVEL)
				;DebugLog("PHYSICS LEVEL HIT");testing			
				; EntityColor(marker(markerCTR),255-(markerCTR*2),0,0);testing
				; if markerCTR=1  EntityColor(marker(markerCTR),255,255,0);testing
				For physicscollisions = 1 To CountCollisions(this\pivot)
					; Get the normal of the surface which the entity collided with. 
					physicsnormalx# = CollisionNX(this\pivot, physicscollisions) 
					physicsnormaly# = CollisionNY(this\pivot, physicscollisions) 
					physicsnormalz# = CollisionNZ(this\pivot, physicscollisions) 
					; Compute the dot product of the entity's motion vector and the normal of the surface collided with. 
					physicsdotproduct# = this\xvel#*physicsnormalx# + this\yvel#*physicsnormaly# + this\zvel#*physicsnormalz# 
					; Calculate the normal force. 
					physicsforceX# = this\force * physicsnormalx# * physicsdotproduct# 
					physicsforceY# = this\force * physicsnormaly# * physicsdotproduct# 
					physicsforceZ# = this\force * physicsnormalz# * physicsdotproduct# 
					; Add the normal force to the direction vector. 
					this\xvel# = this\xvel# + physicsforceX# * this\bounce#
					this\yvel# = this\yvel# + physicsforceY# * this\bounce#
					this\zvel# = this\zvel# + physicsforceZ# * this\bounce#
					;Get vector from center to collision
					physicscenterx#=(CollisionX(this\pivot,physicscollisions)-this\x)
					physicscentery#=(CollisionY(this\pivot,physicscollisions)-this\y)
					physicscenterz#=(CollisionZ(this\pivot,physicscollisions)-this\z)
					;Cross product
					physicscrossproductx# = ( physicscentery * this\zvel ) - ( physicscenterz * this\yvel ) 
					physicscrossproducty# = ( physicscenterz * this\xvel ) - ( physicscenterx * this\zvel ) 
					physicscrossproductz# = ( physicscenterx * this\yvel ) - ( physicscentery * this\xvel ) 				
					AlignToVector(physics_Pivot,physicscrossproductx,physicscrossproducty,physicscrossproductz,1)
				Next

				physicscollisionnx# = CollisionNX(this\pivot,1) 
				physicscollisionny# = CollisionNY(this\pivot,1) 
				physicscollisionnz# = CollisionNZ(this\pivot,1)				
				
				AlignToVector(this\pivot,physicscollisionnx#,physicscollisionny#,physicscollisionnz#,2,0.5)
				this\vel#=Sqr(this\xvel2*this\xvel2 + this\yvel2*this\yvel2 + this\zvel2*this\zvel2)
				;slow down due to friction
				this\xvel#=this\xvel*this\friction#
				this\yvel#=this\yvel*this\friction#
				this\zvel#=this\zvel*this\friction#
			EndIf
			;ball rotation	
			EntityParent(this\entity,physics_Pivot)
			TurnEntity(physics_Pivot,-this\vel#*(180/Pi)/this\radius#,0,0)
			EntityParent(this\entity,0)
			this\ax#=0
			this\ay#=0
			this\az#=0	
		EndIf	
	Next
	
End Function

Function physicsWrite(this.physics,physicsfile)
	;Purpose: Writes physics Property Values to File
	;Parameters: physics Instance, physicsfile=filehandle
	;Return: None
	WriteInt(physicsfile,this\pivot%)
	WriteInt(physicsfile,this\entity%)
	WriteFloat(physicsfile,this\x#)
	WriteFloat(physicsfile,this\y#)
	WriteFloat(physicsfile,this\z# )
	WriteFloat(physicsfile,this\xvel#)
	WriteFloat(physicsfile,this\yvel#)
	WriteFloat(physicsfile,this\zvel# )
	WriteFloat(physicsfile,this\ax#)
	WriteFloat(physicsfile,this\ay#)
	WriteFloat(physicsfile,this\az# )
	WriteFloat(physicsfile,this\radius#)
	WriteFloat(physicsfile,this\bounce#)
	WriteFloat(physicsfile,this\vel#)
	WriteFloat(physicsfile,this\xvel2#)
	WriteFloat(physicsfile,this\yvel2#)
	WriteFloat(physicsfile,this\zvel2#)
	WriteFloat(physicsfile,this\friction#)
	WriteFloat(physicsfile,this\force#)
End Function

Function physicsRead.physics(physicsfile)
	;Purpose: Reads File To physics Property Values
	;Parameters: physicsfile=filehandle
	;Return: physics Instance
	this.physics=New physics
	this\pivot%=ReadInt(physicsfile)
	this\entity%=ReadInt(physicsfile)
	this\x#=ReadFloat(physicsfile)
	this\y#=ReadFloat(physicsfile)
	this\z# =ReadFloat(physicsfile)
	this\xvel#=ReadFloat(physicsfile)
	this\yvel#=ReadFloat(physicsfile)
	this\zvel# =ReadFloat(physicsfile)
	this\ax#=ReadFloat(physicsfile)
	this\ay#=ReadFloat(physicsfile)
	this\az# =ReadFloat(physicsfile)
	this\radius#=ReadFloat(physicsfile)
	this\bounce#=ReadFloat(physicsfile)
	this\vel#=ReadFloat(physicsfile)
	this\xvel2#=ReadFloat(physicsfile)
	this\yvel2#=ReadFloat(physicsfile)
	this\zvel2#=ReadFloat(physicsfile)
	this\friction#=ReadFloat(physicsfile)
	this\force#=ReadFloat(physicsfile)
	Return this
End Function

Function physicsSave(physicsfilename$="physics.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: physicsfilename$= Name of File
	;Return: None
	physicsfile=WriteFile(physicsfilename$)
	For this.physics= Each physics
		physicsWrite(this,physicsfile)
	Next
	CloseFile(physicsfile)
End Function

Function physicsOpen(physicsfilename$="physics.dat")
	;Purpose: Opens a Object Property file
	;Parameters: physicsfilename$=Name of Property File
	;Return: None
	physicsfile=ReadFile(physicsfilename$)
	Repeat
		physicsRead(physicsfile)
	Until Eof(physicsfile)
	CloseFile(physicsfile)
End Function

Function physicsCopy.physics(this.physics)
	;Purpose: Creates a Copy of a physics Instance
	;Parameters: physics Instance
	;Return: Copy of physics Instance
	copy.physics=physicsNew()
	copy\pivot%=this\pivot%
	copy\entity%=this\entity%
	copy\x#=this\x#
	copy\y#=this\y#
	copy\z# =this\z# 
	copy\xvel#=this\xvel#
	copy\yvel#=this\yvel#
	copy\zvel# =this\zvel# 
	copy\ax#=this\ax#
	copy\ay#=this\ay#
	copy\az# =this\az# 
	copy\radius#=this\radius#
	copy\bounce#=this\bounce#
	copy\vel#=this\vel#
	copy\xvel2#=this\xvel2#
	copy\yvel2#=this\yvel2#
	copy\zvel2#=this\zvel2#
	copy\friction#=this\friction#
	copy\force#=this\force#
	Return copy
End Function

Function physicsMimic(this.physics,mimic.physics)
	;Purpose: Copies Property Values from one physics Instance To another
	;Parameters: physics Instance
	;Return: None
	mimic\pivot%=this\pivot%
	mimic\entity%=this\entity%
	mimic\x#=this\x#
	mimic\y#=this\y#
	mimic\z# =this\z# 
	mimic\xvel#=this\xvel#
	mimic\yvel#=this\yvel#
	mimic\zvel# =this\zvel# 
	mimic\ax#=this\ax#
	mimic\ay#=this\ay#
	mimic\az# =this\az# 
	mimic\radius#=this\radius#
	mimic\bounce#=this\bounce#
	mimic\vel#=this\vel#
	mimic\xvel2#=this\xvel2#
	mimic\yvel2#=this\yvel2#
	mimic\zvel2#=this\zvel2#
	mimic\friction#=this\friction#
	mimic\force#=this\force#
End Function

Function physicsSet(this.physics,physicsentity%,physicsradius#=1,physicstravelspeed#,physicsbounce#=0.9,physicsfriction#=.98,physicsforce#=-2.0)
	;Purpose: Set physics Property Values
	;Parameters: physics Instance and Properties
	;Return: None
	this\entity=physicsentity
	this\xvel#=0.0
	this\yvel#=0.0
	this\zvel# =0.0
	this\vel#=0.0
	this\xvel2#=0.0
	this\yvel2#=0.0
	this\zvel2#=0.0
	this\radius#=physicsradius#
	this\bounce#=physicsbounce#
	this\friction#=physicsfriction#
	this\force#=physicsforce#
	EntityType(this\pivot,GAME_COLLISIONTYPE_PHYSICS)
	EntityRadius(this\pivot,this\radius,this\radius*1.5)	
	PositionEntity(this\pivot,EntityX(this\entity,True),EntityY(this\entity,True),EntityZ(this\entity,True))
	;RotateEntity(this\pivot,EntityPitch(this\entity,True),EntityYaw(this\entity,True),EntityRoll(this\entity,True))													
	ResetEntity(this\pivot);required after position to prevent collision sticking 
	TFormVector(0.0,0.0,physicstravelspeed#,this\entity%,0) ;set directional vector & speed
	this\ax#=TFormedX()
	this\ay#=TFormedY()
	this\az#=TFormedZ()
End Function

Function physicsControl(this.physics)
	;Purpose: Directional Control for physics
	;Parameters: physics object
	;Return: None
	If KeyDown(200)
		TFormVector(0,0,0.03,this\pivot,0)
		this\ax#=TFormedX()
		this\ay#=TFormedY()
		this\az#=TFormedZ()
	EndIf
	If KeyDown(208)
		this\xvel#=this\xvel#*.94
		this\yvel#=this\yvel#*.94
		this\zvel#=this\zvel#*.94
	EndIf
	If KeyDown(57)
		TFormVector(0,0.05,0,this\pivot,0)
		this\ax#=this\ax+TFormedX()
		this\ay#=this\ay+TFormedY()
		this\az#=this\az+TFormedZ()
	EndIf
	If KeyDown(203) TurnEntity(this\pivot,0,2,0)
	If KeyDown(205) 	TurnEntity(this\pivot,0,-2,0)
End Function

Function physicsCamera(this.physics,physicscamera,physicscameramode,physicscamerax#,physicscameray#,physicscameraz#,physicscameraaimx#=0,physicscameraaimy#=0,physicscameraaimz#=0,physicscamerasmooth#=0.1,roll#=0)
	;Purpose: Main Loop Update. Updates all physics Instances.
	;Parameters: None
	;Return: None
	If physicscameramode 
		PointEntity(physicscamera,this\pivot) 	
	Else
		TFormPoint(physicscamerax#,physicscameray#,physicscameraz#,this\pivot,0)
		dx#=(TFormedX()-EntityX(physicscamera))*physicscamerasmooth#
		dy#=(TFormedY()-EntityY(physicscamera))*physicscamerasmooth#
		dz#=(TFormedZ()-EntityZ(physicscamera))*physicscamerasmooth#
		TranslateEntity(physicscamera,dx,dy,dz)
		TFormPoint(physicscameraaimx#,physicscameraaimy#,physicscameraaimz#,this\pivot,0)
		dx# = EntityX(physicscamera)-TFormedX()
		dy# = EntityY(physicscamera)-TFormedY()
		dz# = EntityZ(physicscamera)-TFormedZ()
		dist#=Sqr#((dx#*dx#)+(dz#*dz#))
		pitch#=ATan2(dy#,dist#)
		yaw#=ATan2(dx#,-dz#)
		RotateEntity(physicscamera,pitch#,yaw#,roll#)
	EndIf	
End Function

Function physicsStackPush(this.physics)
	;Purpose: Pushes physics object into physics_stack
	;Parameters: TBD
	;Return: TBD
	HideEntity(this\pivot)
	this\entity=0
	physics_stack(physics_stackPTR%)=this
	physics_stackPTR%=physics_stackPTR%+1	
End Function

Function physicsStackPop.physics()
	;Purpose: Pops physics object out of physics_stack
	;Parameters: TBD
	;Return: TBD
	physics_stackPTR%=physics_stackPTR%-1	
	 this.physics=physics_stack(physics_stackPTR%)
	ShowEntity(this\pivot)
	 Return this
End Function

Function physicsMethod.physics(this.physics)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function


	

	