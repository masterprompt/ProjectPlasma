;============================
;REACTOR CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================
;Include "recycler.bb"

;============================
;CONSTANTS
;============================
Const REACTOR_MAX%=256
Const REACTOR_PARTICLE_MAX%=64
Const REACTOR_ENTITYTYPE_MESH=1
Const REACTOR_ENTITYTYPE_SPRITE=2
Const REACTOR_ENTITYTYPE_ANIMSPRITE=3
Const REACTOR_EFFECT_NONE=0
Const REACTOR_EFFECT_MOVE=1
Const REACTOR_EFFECT_TURN=2
Const REACTOR_EFFECT_SCALE=4
Const REACTOR_EFFECT_COLOR=8
Const REACTOR_EFFECT_ALPHA=16
Const REACTOR_EFFECT_BLEND=32
Const REACTOR_EFFECT_ANIMATE=64

;============================
;GLOBALS
;============================
Global reactor_ID.reactor[REACTOR_MAX%];Primary Key Object Pointer
Global reactor_Index%;Primary Key ID Management Counter
;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type reactor
	;Purpose: 
	;Properties:
	Field ID%;
	Field audioID%;
	Field recyclerID%;
	Field copies%;
	Field filename$;
	Field texture%
	Field framewidth%;
	Field frameheight%;
	Field firstframe%;
	Field framecount% ;	
	Field entitytype%;
	Field entity%;
	Field x1#;
	Field y1#;
	Field z1#;
	Field x2#;
	Field y2#;
	Field z2#;
	Field pitch1#;
	Field yaw1#;
	Field roll1#;
	Field pitch2#;
	Field yaw2#;
	Field roll2#;
	Field xscale1#;
	Field yscale1#;
	Field zscale1#;
	Field xscale2#;
	Field yscale2#;
	Field zscale2#;
	Field xspeed1#;
	Field yspeed1#;
	Field zspeed1#;
	Field xspeed2#;
	Field yspeed2#;
	Field zspeed2#;
	Field red1#;
	Field green1#;
	Field blue1#;
	Field red2#;
	Field green2#;
	Field blue2#;
	Field alpha1#;
	Field alpha2#;
	Field pitchfactor#;
	Field yawfactor#;
	Field rollfactor#;
	Field xscalefactor#;
	Field yscalefactor#;
	Field zscalefactor#;
	Field redfactor#;
	Field greenfactor#;
	Field bluefactor#;
	Field alphafactor#;	
	Field blend1%;
	Field blend2%;
	Field life%;
	Field effect%;
	Field fx%;
	Field mode%;
	Field blendfactor%;
	Field light%;
	Field lighttype%;
	Field lightrange1#;
	Field lightrange2#;
	Field lightinnerangle1#;
	Field lightinnerangle2#;
	Field lightouterangle1#;
	Field lightouterangle2#;
	Field lightred1#;
	Field lightgreen1#;
	Field lightblue1#;
	Field lightred2#;
	Field lightgreen2#;
	Field lightblue2#;	
End Type

;============================
;METHODS
;============================
Function reactorStart(reactorfilename$="projectile.dat")
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	reactorOpen(reactorfilename$)
	;create recycler copies
	For this.reactor= Each reactor
		Select this\EntityType%
			Case REACTOR_ENTITYTYPE_MESH
				this\entity%=LoadMesh(this\filename$)
			Case REACTOR_ENTITYTYPE_SPRITE
				this\entity%=LoadSprite(this\filename$)
			Case REACTOR_ENTITYTYPE_ANIMSPRITE
				this\texture%=LoadAnimTexture(this\filename$,2,this\framewidth%,this\frameheight%,this\firstframe%,this\framecount%)
				this\entity%=CreateSprite()
				EntityTexture(this\entity%,this\texture%)
		End Select
		;appearancefx
		EntityFX(this\entity%,1+4+8)
		EntityBlend(this\entity%,this\blend1)
		HideEntity(this\entity%)
		;create recycler and store object key
		this\recyclerID%=recyclerCreate(REACTOR_PARTICLE_MAX%)
		;create recycler entity copies for stack push/pop 
		recyclerEntityCopy(recycler_ID[this\recyclerID%],this\entity%,REACTOR_PARTICLE_MAX%)
	Next
End Function

Function reactorStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.reactor=Each reactor
		reactorDelete(this)
	Next
End Function

Function reactorNew.reactor()
	;Purpose: CONSTRUCTOR - Creates reactor Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: reactor Instance
	this.reactor=New reactor
	this\ID%=0
	this\audioID%=0
	this\recyclerID%=0
	this\copies%=64
	this\filename$=""
	this\framewidth%=0
	this\frameheight%=0
	this\firstframe%=0
	this\framecount% =0
	this\EntityType%=2
	this\entity%=0
	this\x1#=0.0
	this\y1#=0.0
	this\z1#=0.0
	this\x2#=0.0
	this\y2#=0.0
	this\z2#=0.0
	this\pitch1#=0.0
	this\yaw1#=0.0
	this\roll1#=0.0
	this\pitch2#=0.0
	this\yaw2#=0.0
	this\roll2#=0.0
	this\xscale1#=1.0
	this\yscale1#=1.0
	this\zscale1#=1.0
	this\xscale2#=1.0
	this\yscale2#=1.0
	this\zscale2#=1.0
	this\xspeed1#=1.0
	this\yspeed1#=1.0
	this\zspeed1#=1.0
	this\xspeed2#=1.0
	this\yspeed2#=1.0
	this\zspeed2#=1.0
	this\red1#=255.0
	this\green1#=255.0
	this\blue1#=255.0
	this\red2#=255.0
	this\green2#=255.0
	this\blue2#=255.0
	this\alpha1#=1.0
	this\alpha2#=1.0
	this\pitchfactor#=1.0
	this\yawfactor#=1.0
	this\rollfactor#=1.0
	this\xscalefactor#=1.0
	this\yscalefactor#=1.0
	this\zscalefactor#=1.0
	this\redfactor#=1.0
	this\greenfactor#=1.0
	this\bluefactor#=1.0
	this\alphafactor#=0.1	
	this\blend1%=3
	this\blend2%=3
	this\life%=0
	this\effect%=0
	this\fx%=0
	this\mode%=0
	this\blendfactor%=0
	this\light%=0
	this\lighttype%=0
	this\lightrange1#=0.0
	this\lightrange2#=0.0
	this\lightinnerangle1#=0.0
	this\lightinnerangle2#=0.0
	this\lightouterangle1#=0.0
	this\lightouterangle2#=0.0
	this\lightred1#=0.0
	this\lightgreen1#=0.0
	this\lightblue1#=0.0
	this\lightred2#=0.0
	this\lightgreen2#=0.0
	this\lightblue2#=0.0	
	reactor_Index=reactor_Index+1
	this\id%=reactor_Index
	reactor_ID[this\id%]=this
	Return this
End Function

Function reactorDelete(this.reactor)
	;Purpose: DESTRUCTOR - Removes reactor Instance
	;Parameters: reactor Instance
	;Return: None
	FreeEntity this\entity%
	this\filename$=""
	Delete this
End Function

Function reactorUpdate()
	;Purpose: Main Loop Update. Updates all reactor Instances.
	;Parameters: None
	;Return: None
	For this.reactor=Each reactor
		;Do stuff to 'this' here!
	Next
End Function

Function reactorWrite(this.reactor,reactorfile)
	;Purpose: Writes reactor Property Values to File
	;Parameters: reactor Instance, reactorfile=filehandle
	;Return: None
	WriteInt(reactorfile,this\ID%)
	WriteInt(reactorfile,this\audioID%)
	WriteInt(reactorfile,this\recyclerID%)
	WriteInt(reactorfile,this\copies%)
	WriteString(reactorfile,this\filename$)
	WriteInt(reactorfile,this\framewidth%)
	WriteInt(reactorfile,this\frameheight%)
	WriteInt(reactorfile,this\firstframe%)
	WriteInt(reactorfile,this\framecount% )	
	WriteInt(reactorfile,this\entitytype%)
	WriteInt(reactorfile,this\entity%)
	WriteFloat(reactorfile,this\x1#)
	WriteFloat(reactorfile,this\y1#)
	WriteFloat(reactorfile,this\z1#)
	WriteFloat(reactorfile,this\x2#)
	WriteFloat(reactorfile,this\y2#)
	WriteFloat(reactorfile,this\z2#)
	WriteFloat(reactorfile,this\pitch1#)
	WriteFloat(reactorfile,this\yaw1#)
	WriteFloat(reactorfile,this\roll1#)
	WriteFloat(reactorfile,this\pitch2#)
	WriteFloat(reactorfile,this\yaw2#)
	WriteFloat(reactorfile,this\roll2#)
	WriteFloat(reactorfile,this\xscale1#)
	WriteFloat(reactorfile,this\yscale1#)
	WriteFloat(reactorfile,this\zscale1#)
	WriteFloat(reactorfile,this\xscale2#)
	WriteFloat(reactorfile,this\yscale2#)
	WriteFloat(reactorfile,this\zscale2#)
	WriteFloat(reactorfile,this\xspeed1#)
	WriteFloat(reactorfile,this\yspeed1#)
	WriteFloat(reactorfile,this\zspeed1#)
	WriteFloat(reactorfile,this\xspeed2#)
	WriteFloat(reactorfile,this\yspeed2#)
	WriteFloat(reactorfile,this\zspeed2#)
	WriteFloat(reactorfile,this\red1#)
	WriteFloat(reactorfile,this\green1#)
	WriteFloat(reactorfile,this\blue1#)
	WriteFloat(reactorfile,this\red2#)
	WriteFloat(reactorfile,this\green2#)
	WriteFloat(reactorfile,this\blue2#)
	WriteFloat(reactorfile,this\alpha1#)
	WriteFloat(reactorfile,this\alpha2#)
	WriteFloat(reactorfile,this\pitchfactor#)
	WriteFloat(reactorfile,this\yawfactor#)
	WriteFloat(reactorfile,this\rollfactor#)
	WriteFloat(reactorfile,this\xscalefactor#)
	WriteFloat(reactorfile,this\yscalefactor#)
	WriteFloat(reactorfile,this\zscalefactor#)
	WriteFloat(reactorfile,this\redfactor#)
	WriteFloat(reactorfile,this\greenfactor#)
	WriteFloat(reactorfile,this\bluefactor#)
	WriteFloat(reactorfile,this\alphafactor#)	
	WriteInt(reactorfile,this\blend1%)
	WriteInt(reactorfile,this\blend2%)
	WriteInt(reactorfile,this\life%)
	WriteInt(reactorfile,this\effect%)
	WriteInt(reactorfile,this\fx%)
	WriteInt(reactorfile,this\mode%)
	WriteInt(reactorfile,this\blendfactor%)
	WriteInt(reactorfile,this\light%)
	WriteInt(reactorfile,this\lighttype%)
	WriteFloat(reactorfile,this\lightrange1#)
	WriteFloat(reactorfile,this\lightrange2#)
	WriteFloat(reactorfile,this\lightinnerangle1#)
	WriteFloat(reactorfile,this\lightinnerangle2#)
	WriteFloat(reactorfile,this\lightouterangle1#)
	WriteFloat(reactorfile,this\lightouterangle2#)
	WriteFloat(reactorfile,this\lightred1#)
	WriteFloat(reactorfile,this\lightgreen1#)
	WriteFloat(reactorfile,this\lightblue1#)
	WriteFloat(reactorfile,this\lightred2#)
	WriteFloat(reactorfile,this\lightgreen2#)
	WriteFloat(reactorfile,this\lightblue2#)	
End Function

Function reactorRead.reactor(reactorfile)
	;Purpose: Reads File To reactor Property Values
	;Parameters: reactorfile=filehandle
	;Return: reactor Instance
	this.reactor=New reactor
	this\ID%=ReadInt(reactorfile)
	this\audioID%=ReadInt(reactorfile)
	this\recyclerID%=ReadInt(reactorfile)
	this\copies%=ReadInt(reactorfile)
	this\filename$=ReadString(reactorfile)
	this\framewidth%=ReadInt(reactorfile)
	this\frameheight%=ReadInt(reactorfile)
	this\firstframe%=ReadInt(reactorfile)
	this\framecount% =ReadInt(reactorfile)	
	this\entitytype%=ReadInt(reactorfile)
	this\entity%=ReadInt(reactorfile)
	this\x1#=ReadFloat(reactorfile)
	this\y1#=ReadFloat(reactorfile)
	this\z1#=ReadFloat(reactorfile)
	this\x2#=ReadFloat(reactorfile)
	this\y2#=ReadFloat(reactorfile)
	this\z2#=ReadFloat(reactorfile)
	this\pitch1#=ReadFloat(reactorfile)
	this\yaw1#=ReadFloat(reactorfile)
	this\roll1#=ReadFloat(reactorfile)
	this\pitch2#=ReadFloat(reactorfile)
	this\yaw2#=ReadFloat(reactorfile)
	this\roll2#=ReadFloat(reactorfile)
	this\xscale1#=ReadFloat(reactorfile)
	this\yscale1#=ReadFloat(reactorfile)
	this\zscale1#=ReadFloat(reactorfile)
	this\xscale2#=ReadFloat(reactorfile)
	this\yscale2#=ReadFloat(reactorfile)
	this\zscale2#=ReadFloat(reactorfile)
	this\xspeed1#=ReadFloat(reactorfile)
	this\yspeed1#=ReadFloat(reactorfile)
	this\zspeed1#=ReadFloat(reactorfile)
	this\xspeed2#=ReadFloat(reactorfile)
	this\yspeed2#=ReadFloat(reactorfile)
	this\zspeed2#=ReadFloat(reactorfile)
	this\red1#=ReadFloat(reactorfile)
	this\green1#=ReadFloat(reactorfile)
	this\blue1#=ReadFloat(reactorfile)
	this\red2#=ReadFloat(reactorfile)
	this\green2#=ReadFloat(reactorfile)
	this\blue2#=ReadFloat(reactorfile)
	this\alpha1#=ReadFloat(reactorfile)
	this\alpha2#=ReadFloat(reactorfile)
	this\pitchfactor#=ReadFloat(reactorfile)
	this\yawfactor#=ReadFloat(reactorfile)
	this\rollfactor#=ReadFloat(reactorfile)
	this\xscalefactor#=ReadFloat(reactorfile)
	this\yscalefactor#=ReadFloat(reactorfile)
	this\zscalefactor#=ReadFloat(reactorfile)
	this\redfactor#=ReadFloat(reactorfile)
	this\greenfactor#=ReadFloat(reactorfile)
	this\bluefactor#=ReadFloat(reactorfile)
	this\alphafactor#=ReadFloat(reactorfile)	
	this\blend1%=ReadInt(reactorfile)
	this\blend2%=ReadInt(reactorfile)
	this\life%=ReadInt(reactorfile)
	this\effect%=ReadInt(reactorfile)
	this\fx%=ReadInt(reactorfile)
	this\mode%=ReadInt(reactorfile)
	this\blendfactor%=ReadInt(reactorfile)
	this\light%=ReadInt(reactorfile)
	this\lighttype%=ReadInt(reactorfile)
	this\lightrange1#=ReadFloat(reactorfile)
	this\lightrange2#=ReadFloat(reactorfile)
	this\lightinnerangle1#=ReadFloat(reactorfile)
	this\lightinnerangle2#=ReadFloat(reactorfile)
	this\lightouterangle1#=ReadFloat(reactorfile)
	this\lightouterangle2#=ReadFloat(reactorfile)
	this\lightred1#=ReadFloat(reactorfile)
	this\lightgreen1#=ReadFloat(reactorfile)
	this\lightblue1#=ReadFloat(reactorfile)
	this\lightred2#=ReadFloat(reactorfile)
	this\lightgreen2#=ReadFloat(reactorfile)
	this\lightblue2#=ReadFloat(reactorfile)
	reactor_ID[this\id%]=this
	Return this
End Function

Function reactorSave(reactorfilename$="reactor.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: reactorfilename$= Name of File
	;Return: None
	reactorfile=WriteFile(reactorfilename$)
	For this.reactor= Each reactor
		reactorWrite(this,reactorfile)
	Next
	CloseFile(reactorfile)
End Function

Function reactorOpen(reactorfilename$="reactor.dat")
	;Purpose: Opens a Object Property file
	;Parameters: reactorfilename$=Name of Property File
	;Return: None
	reactorfile=ReadFile(reactorfilename$)
	Repeat
		reactorRead(reactorfile)
	Until Eof(reactorfile)
	CloseFile(reactorfile)
End Function

Function reactorCopy.reactor(this.reactor)
	;Purpose: Creates a Copy of a reactor Instance
	;Parameters: reactor Instance
	;Return: Copy of reactor Instance
	copy.reactor=reactorNew()
	copy\ID%=this\ID%
	copy\audioID%=this\audioID%
	copy\recyclerID%=this\recyclerID%
	copy\copies%=this\copies%
	copy\filename$=this\filename$
	copy\framewidth%=this\framewidth%
	copy\frameheight%=this\frameheight%
	copy\firstframe%=this\firstframe%
	copy\framecount% =this\framecount% 	
	copy\entitytype%=this\entitytype%
	copy\entity%=this\entity%
	copy\x1#=this\x1#
	copy\y1#=this\y1#
	copy\z1#=this\z1#
	copy\x2#=this\x2#
	copy\y2#=this\y2#
	copy\z2#=this\z2#
	copy\pitch1#=this\pitch1#
	copy\yaw1#=this\yaw1#
	copy\roll1#=this\roll1#
	copy\pitch2#=this\pitch2#
	copy\yaw2#=this\yaw2#
	copy\roll2#=this\roll2#
	copy\xscale1#=this\xscale1#
	copy\yscale1#=this\yscale1#
	copy\zscale1#=this\zscale1#
	copy\xscale2#=this\xscale2#
	copy\yscale2#=this\yscale2#
	copy\zscale2#=this\zscale2#
	copy\xspeed1#=this\xspeed1#
	copy\yspeed1#=this\yspeed1#
	copy\zspeed1#=this\zspeed1#
	copy\xspeed2#=this\xspeed2#
	copy\yspeed2#=this\yspeed2#
	copy\zspeed2#=this\zspeed2#
	copy\red1#=this\red1#
	copy\green1#=this\green1#
	copy\blue1#=this\blue1#
	copy\red2#=this\red2#
	copy\green2#=this\green2#
	copy\blue2#=this\blue2#
	copy\alpha1#=this\alpha1#
	copy\alpha2#=this\alpha2#
	copy\pitchfactor#=this\pitchfactor#
	copy\yawfactor#=this\yawfactor#
	copy\rollfactor#=this\rollfactor#
	copy\xscalefactor#=this\xscalefactor#
	copy\yscalefactor#=this\yscalefactor#
	copy\zscalefactor#=this\zscalefactor#
	copy\redfactor#=this\redfactor#
	copy\greenfactor#=this\greenfactor#
	copy\bluefactor#=this\bluefactor#
	copy\alphafactor#=this\alphafactor#	
	copy\blend1%=this\blend1%
	copy\blend2%=this\blend2%
	copy\life%=this\life%
	copy\effect%=this\effect%
	copy\fx%=this\fx%
	copy\mode%=this\mode%
	copy\blendfactor%=this\blendfactor%
	copy\light%=this\light%
	copy\lighttype%=this\lighttype%
	copy\lightrange1#=this\lightrange1#
	copy\lightrange2#=this\lightrange2#
	copy\lightinnerangle1#=this\lightinnerangle1#
	copy\lightinnerangle2#=this\lightinnerangle2#
	copy\lightouterangle1#=this\lightouterangle1#
	copy\lightouterangle2#=this\lightouterangle2#
	copy\lightred1#=this\lightred1#
	copy\lightgreen1#=this\lightgreen1#
	copy\lightblue1#=this\lightblue1#
	copy\lightred2#=this\lightred2#
	copy\lightgreen2#=this\lightgreen2#
	copy\lightblue2#=this\lightblue2#
	Return copy
End Function

Function reactorMimic(this.reactor,mimic.reactor)
	;Purpose: Copies Property Values from one reactor Instance To another
	;Parameters: reactor Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\audioID%=this\audioID%
	mimic\recyclerID%=this\recyclerID%
	mimic\copies%=this\copies%
	mimic\filename$=this\filename$
	mimic\framewidth%=this\framewidth%
	mimic\frameheight%=this\frameheight%
	mimic\firstframe%=this\firstframe%
	mimic\framecount% =this\framecount% 	
	mimic\entitytype%=this\entitytype%
	mimic\entity%=this\entity%
	mimic\x1#=this\x1#
	mimic\y1#=this\y1#
	mimic\z1#=this\z1#
	mimic\x2#=this\x2#
	mimic\y2#=this\y2#
	mimic\z2#=this\z2#
	mimic\pitch1#=this\pitch1#
	mimic\yaw1#=this\yaw1#
	mimic\roll1#=this\roll1#
	mimic\pitch2#=this\pitch2#
	mimic\yaw2#=this\yaw2#
	mimic\roll2#=this\roll2#
	mimic\xscale1#=this\xscale1#
	mimic\yscale1#=this\yscale1#
	mimic\zscale1#=this\zscale1#
	mimic\xscale2#=this\xscale2#
	mimic\yscale2#=this\yscale2#
	mimic\zscale2#=this\zscale2#
	mimic\xspeed1#=this\xspeed1#
	mimic\yspeed1#=this\yspeed1#
	mimic\zspeed1#=this\zspeed1#
	mimic\xspeed2#=this\xspeed2#
	mimic\yspeed2#=this\yspeed2#
	mimic\zspeed2#=this\zspeed2#
	mimic\red1#=this\red1#
	mimic\green1#=this\green1#
	mimic\blue1#=this\blue1#
	mimic\red2#=this\red2#
	mimic\green2#=this\green2#
	mimic\blue2#=this\blue2#
	mimic\alpha1#=this\alpha1#
	mimic\alpha2#=this\alpha2#
	mimic\pitchfactor#=this\pitchfactor#
	mimic\yawfactor#=this\yawfactor#
	mimic\rollfactor#=this\rollfactor#
	mimic\xscalefactor#=this\xscalefactor#
	mimic\yscalefactor#=this\yscalefactor#
	mimic\zscalefactor#=this\zscalefactor#
	mimic\redfactor#=this\redfactor#
	mimic\greenfactor#=this\greenfactor#
	mimic\bluefactor#=this\bluefactor#
	mimic\alphafactor#=this\alphafactor#	
	mimic\blend1%=this\blend1%
	mimic\blend2%=this\blend2%
	mimic\life%=this\life%
	mimic\effect%=this\effect%
	mimic\fx%=this\fx%
	mimic\mode%=this\mode%
	mimic\blendfactor%=this\blendfactor%
	mimic\light%=this\light%
	mimic\lighttype%=this\lighttype%
	mimic\lightrange1#=this\lightrange1#
	mimic\lightrange2#=this\lightrange2#
	mimic\lightinnerangle1#=this\lightinnerangle1#
	mimic\lightinnerangle2#=this\lightinnerangle2#
	mimic\lightouterangle1#=this\lightouterangle1#
	mimic\lightouterangle2#=this\lightouterangle2#
	mimic\lightred1#=this\lightred1#
	mimic\lightgreen1#=this\lightgreen1#
	mimic\lightblue1#=this\lightblue1#
	mimic\lightred2#=this\lightred2#
	mimic\lightgreen2#=this\lightgreen2#
	mimic\lightblue2#=this\lightblue2#	
End Function

Function reactorCreate%(reactorID%,reactoraudioID%,reactorrecyclerID%,reactorcopies%,reactorfilename$,reactorentitytype%,reactorentity%,reactorx1#,reactory1#,reactorz1#,reactorx2#,reactory2#,reactorz2#,reactorpitch1#,reactoryaw1#,reactorroll1#,reactorpitch2#,reactoryaw2#,reactorroll2#,reactorxscale1#,reactoryscale1#,reactorzscale1#,reactorxscale2#,reactoryscale2#,reactorzscale2#,reactorxspeed1#,reactoryspeed1#,reactorzspeed1#,reactorxspeed2#,reactoryspeed2#,reactorzspeed2#,reactorred1#,reactorgreen1#,reactorblue1#,reactorred2#,reactorgreen2#,reactorblue2#,reactoralpha1#,reactoralpha2#,reactorpitchfactor#,reactoryawfactor#,reactorrollfactor#,reactorxscalefactor#,reactoryscalefactor#,reactorzscalefactor#,reactorredfactor#,reactorgreenfactor#,reactorbluefactor#,reactoralphafactor#,reactorblend1%,reactorblend2%,reactorlife%,reactoreffect%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.reactor=reactorNew()
	this\ID%=reactorID%
	this\audioID%=reactoraudioID%
	this\recyclerID%=reactorrecyclerID%
	this\copies%=reactorcopies%
	this\filename$=reactorfilename$
	this\framewidth%=reactorframewidth%
	this\frameheight%=reactorframeheight%
	this\firstframe%=reactorfirstframe%
	this\framecount% =reactorframecount% 	
	this\entitytype%=reactorentitytype%
	this\entity%=reactorentity%
	this\x1#=reactorx1#
	this\y1#=reactory1#
	this\z1#=reactorz1#
	this\x2#=reactorx2#
	this\y2#=reactory2#
	this\z2#=reactorz2#
	this\pitch1#=reactorpitch1#
	this\yaw1#=reactoryaw1#
	this\roll1#=reactorroll1#
	this\pitch2#=reactorpitch2#
	this\yaw2#=reactoryaw2#
	this\roll2#=reactorroll2#
	this\xscale1#=reactorxscale1#
	this\yscale1#=reactoryscale1#
	this\zscale1#=reactorzscale1#
	this\xscale2#=reactorxscale2#
	this\yscale2#=reactoryscale2#
	this\zscale2#=reactorzscale2#
	this\xspeed1#=reactorxspeed1#
	this\yspeed1#=reactoryspeed1#
	this\zspeed1#=reactorzspeed1#
	this\xspeed2#=reactorxspeed2#
	this\yspeed2#=reactoryspeed2#
	this\zspeed2#=reactorzspeed2#
	this\red1#=reactorred1#
	this\green1#=reactorgreen1#
	this\blue1#=reactorblue1#
	this\red2#=reactorred2#
	this\green2#=reactorgreen2#
	this\blue2#=reactorblue2#
	this\alpha1#=reactoralpha1#
	this\alpha2#=reactoralpha2#
	this\pitchfactor#=reactorpitchfactor#
	this\yawfactor#=reactoryawfactor#
	this\rollfactor#=reactorrollfactor#
	this\xscalefactor#=reactorxscalefactor#
	this\yscalefactor#=reactoryscalefactor#
	this\zscalefactor#=reactorzscalefactor#
	this\redfactor#=reactorredfactor#
	this\greenfactor#=reactorgreenfactor#
	this\bluefactor#=reactorbluefactor#
	this\alphafactor#=reactoralphafactor#	
	this\blend1%=reactorblend1%
	this\blend2%=reactorblend2%
	this\life%=reactorlife%
	this\effect%=reactoreffect%
	Return this\id
End Function

Function reactorDestroy(reactorid%)
	;Purpose: Remove object by ID
	;Parameters: reactorid%=Object's ID
	;Return: None
	reactorDelete(reactor_ID[reactorid%])
End Function

Function reactorSet(this.reactor,reactorID%,reactoraudioID%,reactorrecyclerID%,reactorcopies%,reactorfilename$,reactorentitytype%,reactorentity%,reactorx1#,reactory1#,reactorz1#,reactorx2#,reactory2#,reactorz2#,reactorpitch1#,reactoryaw1#,reactorroll1#,reactorpitch2#,reactoryaw2#,reactorroll2#,reactorxscale1#,reactoryscale1#,reactorzscale1#,reactorxscale2#,reactoryscale2#,reactorzscale2#,reactorxspeed1#,reactoryspeed1#,reactorzspeed1#,reactorxspeed2#,reactoryspeed2#,reactorzspeed2#,reactorred1#,reactorgreen1#,reactorblue1#,reactorred2#,reactorgreen2#,reactorblue2#,reactoralpha1#,reactoralpha2#,reactorpitchfactor#,reactoryawfactor#,reactorrollfactor#,reactorxscalefactor#,reactoryscalefactor#,reactorzscalefactor#,reactorredfactor#,reactorgreenfactor#,reactorbluefactor#,reactoralphafactor#,reactorblend1%,reactorblend2%,reactorlife%,reactoreffect%)
	;Purpose: Set reactor Property Values
	;Parameters: reactor Instance and Properties
	;Return: None
	this\ID%=reactorID%
	this\audioID%=reactoraudioID%
	this\recyclerID%=reactorrecyclerID%
	this\copies%=reactorcopies%
	this\filename$=reactorfilename$
	this\framewidth%=reactorframewidth%
	this\frameheight%=reactorframeheight%
	this\firstframe%=reactorfirstframe%
	this\framecount% =reactorframecount% 	
	this\entitytype%=reactorentitytype%
	this\entity%=reactorentity%
	this\x1#=reactorx1#
	this\y1#=reactory1#
	this\z1#=reactorz1#
	this\x2#=reactorx2#
	this\y2#=reactory2#
	this\z2#=reactorz2#
	this\pitch1#=reactorpitch1#
	this\yaw1#=reactoryaw1#
	this\roll1#=reactorroll1#
	this\pitch2#=reactorpitch2#
	this\yaw2#=reactoryaw2#
	this\roll2#=reactorroll2#
	this\xscale1#=reactorxscale1#
	this\yscale1#=reactoryscale1#
	this\zscale1#=reactorzscale1#
	this\xscale2#=reactorxscale2#
	this\yscale2#=reactoryscale2#
	this\zscale2#=reactorzscale2#
	this\xspeed1#=reactorxspeed1#
	this\yspeed1#=reactoryspeed1#
	this\zspeed1#=reactorzspeed1#
	this\xspeed2#=reactorxspeed2#
	this\yspeed2#=reactoryspeed2#
	this\zspeed2#=reactorzspeed2#
	this\red1#=reactorred1#
	this\green1#=reactorgreen1#
	this\blue1#=reactorblue1#
	this\red2#=reactorred2#
	this\green2#=reactorgreen2#
	this\blue2#=reactorblue2#
	this\alpha1#=reactoralpha1#
	this\alpha2#=reactoralpha2#
	this\blend1%=reactorblend1%
	this\blend2%=reactorblend2%
	this\life%=reactorlife%
	this\effect%=reactoreffect%
	this\fx%=reactorfx%
	this\mode%=reactormode%
	this\blendfactor%=reactorblendfactor%
	this\light%=reactorlight%
	this\lighttype%=reactorlighttype%
	this\lightrange1#=reactorlightrange1#
	this\lightrange2#=reactorlightrange2#
	this\lightinnerangle1#=reactorlightinnerangle1#
	this\lightinnerangle2#=reactorlightinnerangle2#
	this\lightouterangle1#=reactorlightouterangle1#
	this\lightouterangle2#=reactorlightouterangle2#
	this\lightred1#=reactorlightred1#
	this\lightgreen1#=reactorlightgreen1#
	this\lightblue1#=reactorlightblue1#
	this\lightred2#=reactorlightred2#
	this\lightgreen2#=reactorlightgreen2#
	this\lightblue2#=reactorlightblue2#	
End Function

Function reactorDefaultDatWrite()
	;Purpose: Creates default reactor dat file
	;Parameters: TBD
	;Return: reactor Instance
	
	;1:loading ammo
	this.reactor=reactorNew() 
	this\audioID%=0
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\plasmaspark.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\effect%=REACTOR_EFFECT_NONE
	
	;2:firing ammo 
	this.reactor=reactorNew() 
	this\audioID%=1
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\plasmaflash.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\pitch1#=0.0
	this\yaw1#=0.0
	this\roll1#=60.0
	this\pitch2#=0.0
	this\yaw2#=0.0
	this\roll2#=0.0
	this\xscale1#=.25
	this\yscale1#=1.0
	this\xscale2#=.25
	this\yscale2#=1.0
	this\light=True
	this\lighttype=1	
	this\lightrange1#=.1	
	this\lightgreen1#=255
	this\life%=4
	this\effect%=REACTOR_EFFECT_SCALE+REACTOR_EFFECT_TURN
	
	;3:traveling ammo
	this.reactor=reactorNew() 
	this\audioID%=0
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\plasmaspark.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\life%=1
	this\effect%=REACTOR_EFFECT_NONE
	
	;4:impacting ammo
	this.reactor=reactorNew() 
	this\audioID%=2
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\plasmagas.jpg"
	this\framewidth%=64
	this\frameheight%=64
	this\firstframe%=0
	this\framecount% =39
	this\EntityType%=REACTOR_ENTITYTYPE_ANIMSPRITE
	this\xscale1#=1.0
	this\yscale1#=1.0
	this\xscale2#=2.5
	this\yscale2#=2.5
	this\xscalefactor#=0.083333
	this\yscalefactor#=0.083333
	this\life%=38
	this\effect%=REACTOR_EFFECT_SCALE+REACTOR_EFFECT_ANIMATE
	
	;5:debris
	this.reactor=reactorNew() 
	this\audioID%=0
	this\copies%=64
	this\filename$="..\game\data\3d\reactors\scorch.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\xscale1#=0.50
	this\yscale1#=0.50
	this\xscale2#=0.50
	this\yscale2#=0.50
	this\xscalefactor#=1.0
	this\yscalefactor#=1.0
	this\blend1%=2
	this\life%=256
	this\effect%=REACTOR_EFFECT_SCALE
	
	;6:firing ammo 
	this.reactor=reactorNew() 
	this\audioID%=1
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\plasmaflash2.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\pitch1#=0.0
	this\yaw1#=0.0
	this\roll1#=90.0
	this\pitch2#=0.0
	this\yaw2#=0.0
	this\roll2#=0.0
	this\xscale1#=.25
	this\yscale1#=1.0
	this\xscale2#=.25
	this\yscale2#=1.0
	this\light=True
	this\lighttype=1
	this\lightrange1#=.1	
	this\lightred1#=255
	this\life%=4
	this\effect%=REACTOR_EFFECT_SCALE+REACTOR_EFFECT_TURN
	
	;7
	this.reactor=reactorNew() 
	this\audioID%=0
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\redspark.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\life%=1
	this\effect%=REACTOR_EFFECT_NONE
	
	;8: impacting ammo firesphere
	this.reactor=reactorNew() 
	this\audioID%=2
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\sphere.b3d"
	this\EntityType%=REACTOR_ENTITYTYPE_MESH
	this\xscale1#=.25
	this\yscale1#=.25
	this\zscale1#=.25
	this\xscale2#=4.0
	this\yscale2#=4.0
	this\zscale2=4.0
	this\xscalefactor#=0.1
	this\yscalefactor#=0.1
	this\zscalefactor#=0.1
	this\pitch1#=3.0
	this\yaw1#=6.0
	this\roll1#=9.0
	this\life%=38
	this\effect%=REACTOR_EFFECT_SCALE+REACTOR_EFFECT_TURN
	
	;9: debris
	this.reactor=reactorNew() 
	this\audioID%=0
	this\copies%=64
	this\filename$="..\game\data\3d\reactors\scorch2.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\xscale1#=0.50
	this\yscale1#=0.50
	this\xscale2#=0.50
	this\yscale2#=0.50
	this\xscalefactor#=1.0
	this\yscalefactor#=1.0
	this\blend1%=2
	this\life%=256
	this\effect%=REACTOR_EFFECT_SCALE

	;10:plasma fragger fire particle
	this.reactor=reactorNew() 
	this\audioID%=0
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\bluspark.bmp"
	this\EntityType%=REACTOR_ENTITYTYPE_SPRITE
	this\life%=1
	this\effect%=REACTOR_EFFECT_NONE
	
	;11:impacting ammo
	this.reactor=reactorNew() 
	this\audioID%=2
	this\copies%=8
	this\filename$="..\game\data\3d\reactors\explosion2.jpg"
	this\framewidth%=64
	this\frameheight%=64
	this\firstframe%=0
	this\framecount% =39
	this\EntityType%=REACTOR_ENTITYTYPE_ANIMSPRITE
	this\xscale1#=1.0
	this\yscale1#=1.0
	this\xscale2#=2.5
	this\yscale2#=2.5
	this\xscalefactor#=0.083333
	this\yscalefactor#=0.083333
	this\blend1%=2
	this\life%=38
	this\effect%=REACTOR_EFFECT_SCALE+REACTOR_EFFECT_ANIMATE

	reactorSave()
End Function

Function reactorMethod.reactor(this.reactor)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

If gamedatawrite reactorDefaultDatWrite