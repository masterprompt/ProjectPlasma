;============================
;PROJECTILE CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================
;Include "../reactor/recycler.bb"

;============================
;CONSTANTS
;============================
Const PROJECTILE_MAX%=32
Const PROJECTILE_ENTITYTYPE_MESH=1
Const PROJECTILE_ENTITYTYPE_SPRITE=2
Const PROJECTILE_ENTITYTYPE_ANIMSPRITE=3
Const PROJECTILE_TRAVELTYPE_STRAIGHT=1
Const PROJECTILE_TRAVELTYPE_LOB=2
Const PROJECTILE_TRAVELTYPE_HEATSEEKER=3
Const PROJECTILE_TRAVELTYPE_PATTERN=4
Const PROJECTILE_TRAVELTYPE_SMART=5
Const PROJECTILE_DETONATETYPE_IMPACT=1
Const PROJECTILE_DETONATETYPE_TIMEOUT=2
Const PROJECTILE_ENERGY_SONIC=1
Const PROJECTILE_ENERGY_HEAT=2
Const PROJECTILE_ENERGY_ELECTRICAL=3
Const PROJECTILE_ENERGY_LIGHT=4
Const PROJECTILE_ENERGY_COSMIC=5
Const PROJECTILE_ENERGY_METAL=6

;============================
;GLOBALS
;============================
Global projectile_ID.projectile[PROJECTILE_MAX%];Primary Key Object Pointer
Global projectile_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type projectile
	;Purpose: Profile for various types of projectiles
	;Properties: 
	Field ID%;
	Field loadreactorID%;
	Field firereactorID%;
	Field travelreactorID%;
	Field impactreactorID%;
	Field debrisreactorID%;
	Field loadaudioID%;
	Field fireaudioID%;
	Field travelaudioID%;
	Field impactaudioID%;
	Field debrisaudioID%;
	Field recyclerID%;
	Field copies% ;recycler copies
	Field name$;	
	Field filename$;
	Field texture%
	Field entitytype%;
	Field entity%;
	Field radius#;
	Field blend%
	Field hudimagefilename$
	Field hudimage%	
	Field range#;
	Field traveltype%;
	Field travelspeed#;
	Field travelpitchfactor#;
	Field travelyawfactor#;
	Field travelrollfactor#;
	Field travelanglefactor#;
	Field mass#;	
	Field force#;
	Field bounce# ;
	Field friction#;
	Field xvel# ;
	Field yvel# ;
	Field zvel#;
	Field energy%;energy: sonic|heat/anti-heat|electrical|light|cosmic
	Field detonatetype%;
	Field effect%;
	Field effecttarget%;
	Field effectduration%;
	Field effectarea%;
	Field effectvalue%;
	Field effectdecay#;
End Type

;============================
;METHODS
;============================
Function projectileStart(projectilefilename$="projectile.dat")
	;Purpose: Initialize Class; Loads in Media
	;Parameters: TBD
	;Return: None
	projectileOpen(projectilefilename$)
	;create recycler copies
	For this.projectile= Each projectile
		Select this\EntityType%
			Case PROJECTILE_ENTITYTYPE_MESH
				this\entity%=LoadMesh(this\filename$)
			Case PROJECTILE_ENTITYTYPE_SPRITE
				this\entity%=LoadSprite(this\filename$)
			;Case PROJECTILE_ENTITYTYPE_ANIMSPRITE
			;	this\texture%=LoadAnimTexture(this\filename$,1+2,this\framewidth%,this\frameheight%,this\firstframe%,this\framecount%)
			;	this\entity%=CreateSprite()
			;	EntityTexture(this\entity%,this\texture%)				
		End Select
		;appearancefx
		EntityFX(this\entity%,1+4+8)
		EntityBlend(this\entity%,this\blend)
		HideEntity(this\entity%)
		;create recycler and store object key
		this\recyclerID%=recyclerCreate(PROJECTILE_MAX%)
		;create recycler entity copies for stack push/pop 
		recyclerEntityCopy(recycler_ID[this\recyclerID%],this\entity%,PROJECTILE_MAX%,this\radius#)
	Next
End Function

Function projectileStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.projectile=Each projectile
		;free recycler and entities
		projectileDelete(this)
	Next
End Function

Function projectileNew.projectile()
	;Purpose: CONSTRUCTOR - Creates projectile Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: projectile Instance
	this.projectile=New projectile
	this\ID%=0
	this\loadreactorID%=0
	this\firereactorID%=0
	this\travelreactorID%=0
	this\impactreactorID%=0
	this\debrisreactorID%=0
	this\loadaudioID%=0
	this\fireaudioID%=0
	this\travelaudioID%=0
	this\impactaudioID%=0
	this\debrisaudioID%=0
	this\recyclerID%=0
	this\copies%=0
	this\name$=""
	this\filename$=""
	this\entitytype%=0
	this\entity%=0
	this\radius#=0.0
	this\blend%=0	
	this\hudimagefilename$=""
	this\hudimage%=0	
	this\range#=0.0
	this\traveltype%=0
	this\travelspeed#=0.0
	this\travelpitchfactor#=0.0
	this\travelyawfactor#=0.0
	this\travelrollfactor#=0.0
	this\travelanglefactor#=0.0	
	this\mass#=0.0
	this\force#=0.0
	this\bounce# =0.0
	this\friction#=0.0
	this\xvel# =0.0
	this\yvel# =0.0
	this\zvel#=0.0
	this\energy%=0
	this\detonatetype%=0	
	this\effect%=0
	this\effecttarget%=0
	this\effectduration%=0
	this\effectarea%=0
	this\effectvalue%=0
	this\effectdecay#=0.0
	projectile_Index=projectile_Index+1
	this\id%=projectile_Index
	projectile_ID[this\id%]=this
	Return this
End Function

Function projectileDelete(this.projectile)
	;Purpose: DESTRUCTOR - Removes projectile Instance
	;Parameters: projectile Instance
	;Return: None
	this\effectdecay#=0.0
	this\range#=0.0
	Freeentity this\entity%
	this\filename$=""
	Delete this
End Function

Function projectileUpdate()
	;Purpose: Main Loop Update. Updates all projectile Instances.
	;Parameters: None
	;Return: None
	For this.projectile=Each projectile
		;Do stuff to 'this' here!
	Next
End Function

Function projectileWrite(this.projectile,projectilefile)
	;Purpose: Writes projectile Property Values to File
	;Parameters: projectile Instance, projectilefile=filehandle
	;Return: None
	WriteInt(projectilefile,this\ID%)
	WriteInt(projectilefile,this\loadreactorID%)
	WriteInt(projectilefile,this\firereactorID%)
	WriteInt(projectilefile,this\travelreactorID%)
	WriteInt(projectilefile,this\impactreactorID%)
	WriteInt(projectilefile,this\debrisreactorID%)	
	WriteInt(projectilefile,this\loadaudioID%)
	WriteInt(projectilefile,this\fireaudioID%)
	WriteInt(projectilefile,this\travelaudioID%)
	WriteInt(projectilefile,this\impactaudioID%)
	WriteInt(projectilefile,this\debrisreactorID%)	
	WriteInt(projectilefile,this\recyclerID%)
	WriteInt(projectilefile,this\copies%)
	WriteString(projectilefile,this\name$)	
	WriteString(projectilefile,this\filename$)
	WriteInt(projectilefile,this\EntityType%)
	WriteInt(projectilefile,this\entity%)
	WriteFloat(projectilefile,this\radius#)
	WriteInt(projectilefile,this\blend%)
	WriteString(projectilefile,this\hudimagefilename$)
	WriteInt(projectilefile,this\hudimage%)
	WriteFloat(projectilefile,this\range#)
	WriteInt(projectilefile,this\traveltype%)
	WriteFloat(projectilefile,this\travelspeed#)
	WriteFloat(projectilefile,this\travelpitchfactor#)
	WriteFloat(projectilefile,this\travelyawfactor#)
	WriteFloat(projectilefile,this\travelrollfactor#)	
	WriteFloat(projectilefile,this\travelanglefactor#)	
	WriteFloat(projectilefile,this\mass#)
	WriteFloat(projectilefile,this\force#)
	WriteFloat(projectilefile,this\bounce# )
	WriteFloat(projectilefile,this\friction#)
	WriteFloat(projectilefile,this\xvel# )
	WriteFloat(projectilefile,this\yvel# )
	WriteFloat(projectilefile,this\zvel#)
	WriteInt(projectilefile,this\energy%)
	WriteInt(projectilefile,this\detonatetype%)	
	WriteInt(projectilefile,this\effect%)
	WriteInt(projectilefile,this\effecttarget%)
	WriteInt(projectilefile,this\effectduration%)
	WriteInt(projectilefile,this\effectarea%)
	WriteInt(projectilefile,this\effectvalue%)
	WriteFloat(projectilefile,this\effectdecay#)
End Function

Function projectileRead.projectile(projectilefile)
	;Purpose: Reads File To projectile Property Values
	;Parameters: projectilefile=filehandle
	;Return: projectile Instance
	this.projectile=New projectile
	this\ID%=ReadInt(projectilefile)
	this\loadreactorID%=ReadInt(projectilefile)
	this\firereactorID%=ReadInt(projectilefile)
	this\travelreactorID%=ReadInt(projectilefile)
	this\impactreactorID%=ReadInt(projectilefile)
	this\debrisreactorID%=ReadInt(projectilefile)
	this\loadaudioID%=ReadInt(projectilefile)
	this\fireaudioID%=ReadInt(projectilefile)
	this\travelaudioID%=ReadInt(projectilefile)
	this\impactaudioID%=ReadInt(projectilefile)
	this\debrisaudioID%=ReadInt(projectilefile)
	this\recyclerID%=ReadInt(projectilefile)
	this\copies%=ReadInt(projectilefile)
	this\name$=ReadString(projectilefile)	
	this\filename$=ReadString(projectilefile)
	this\EntityType%=ReadInt(projectilefile)
	this\entity%=ReadInt(projectilefile)
	this\radius#=ReadFloat(projectilefile)
	this\blend%=ReadInt(projectilefile)	
	this\hudimagefilename$=ReadString(projectilefile)
	this\hudimage%=ReadInt(projectilefile)
	this\range#=ReadFloat(projectilefile)
	this\traveltype%=ReadInt(projectilefile)
	this\travelspeed#=ReadFloat(projectilefile)
	this\travelpitchfactor#=ReadFloat(projectilefile)
	this\travelyawfactor#=ReadFloat(projectilefile)
	this\travelrollfactor#=ReadFloat(projectilefile)	
	this\travelanglefactor#=ReadFloat(projectilefile)
	this\mass#=ReadFloat(projectilefile)
	this\force#=ReadFloat(projectilefile)
	this\bounce# =ReadFloat(projectilefile)
	this\friction#=ReadFloat(projectilefile)
	this\xvel# =ReadFloat(projectilefile)
	this\yvel# =ReadFloat(projectilefile)
	this\zvel#=ReadFloat(projectilefile)
	this\energy%=ReadInt(projectilefile)
	this\detonatetype%=ReadInt(projectilefile)	
	this\effect%=ReadInt(projectilefile)
	this\effecttarget%=ReadInt(projectilefile)
	this\effectduration%=ReadInt(projectilefile)
	this\effectarea%=ReadInt(projectilefile)
	this\effectvalue%=ReadInt(projectilefile)
	this\effectdecay#=ReadFloat(projectilefile)
	projectile_ID[this\id%]=this
	Return this
End Function

Function projectileSave(projectilefilename$="projectile.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: projectilefilename$= Name of File
	;Return: None
	projectilefile=WriteFile(projectilefilename$)
	For this.projectile= Each projectile
		projectileWrite(this,projectilefile)
	Next
	CloseFile(projectilefile)
End Function

Function projectileOpen(projectilefilename$="projectile.dat")
	;Purpose: Opens a Object Property file
	;Parameters: projectilefilename$=Name of Property File
	;Return: None
	projectilefile=ReadFile(projectilefilename$)
	Repeat
		projectileRead(projectilefile)
	Until Eof(projectilefile)
	CloseFile(projectilefile)
End Function

Function projectileCopy.projectile(this.projectile)
	;Purpose: Creates a Copy of a projectile Instance
	;Parameters: projectile Instance
	;Return: Copy of projectile Instance
	copy.projectile=projectileNew()
	copy\ID%=this\ID%
	copy\loadreactorID%=this\loadreactorID%
	copy\firereactorID%=this\firereactorID%
	copy\travelreactorID%=this\travelreactorID%
	copy\impactreactorID%=this\impactreactorID%
	copy\debrisreactorID%=this\debrisreactorID%
	copy\loadaudioID%=this\loadaudioID%
	copy\fireaudioID%=this\fireaudioID%
	copy\travelaudioID%=this\travelaudioID%
	copy\impactaudioID%=this\impactaudioID%
	copy\debrisaudioID%=this\debrisaudioID%
	copy\recyclerID%=this\recyclerID%
	copy\copies=this\copies
	copy\name$=this\name$	
	copy\filename$=this\filename$
	copy\entitytype%=this\entitytype%
	copy\entity%=this\entity%
	copy\radius#=this\radius#
	copy\blend%=this\blend%
	copy\hudimagefilename$=this\hudimagefilename$
	copy\hudimage%=this\hudimage%	
	copy\range#=this\range#
	copy\traveltype%=this\traveltype%
	copy\travelspeed#=this\travelspeed#
	copy\travelpitchfactor#=this\travelpitchfactor#
	copy\travelyawfactor#=this\travelyawfactor#
	copy\travelrollfactor#=this\travelrollfactor#
	copy\travelanglefactor#=this\travelanglefactor#
	copy\mass#=this\mass#
	copy\force#=this\force#
	copy\bounce# =this\bounce# 
	copy\friction#=this\friction#
	copy\xvel# =this\xvel# 
	copy\yvel# =this\yvel# 
	copy\zvel#=this\zvel#	
	copy\energy%=this\energy%	
	copy\detonatetype%=this\detonatetype%	
	copy\effect%=this\effect%
	copy\effecttarget%=this\effecttarget%
	copy\effectduration%=this\effectduration%
	copy\effectarea%=this\effectarea%
	copy\effectvalue%=this\effectvalue%
	copy\effectdecay#=this\effectdecay#
	Return copy
End Function

Function projectileMimic(this.projectile,mimic.projectile)
	;Purpose: Copies Property Values from one projectile Instance To another
	;Parameters: projectile Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\loadreactorID%=this\loadreactorID%
	mimic\firereactorID%=this\firereactorID%
	mimic\travelreactorID%=this\travelreactorID%
	mimic\impactreactorID%=this\impactreactorID%
	mimic\debrisreactorID%=this\debrisreactorID%
	mimic\loadaudioID%=this\loadaudioID%
	mimic\fireaudioID%=this\fireaudioID%
	mimic\travelaudioID%=this\travelaudioID%
	mimic\impactaudioID%=this\impactaudioID%
	mimic\debrisaudioID%=this\debrisaudioID%
	mimic\recyclerID%=this\recyclerID%
	mimic\copies%=this\copies
	mimic\name$=this\name$	
	mimic\filename$=this\filename$
	mimic\entitytype%=this\entitytype%
	mimic\entity%=this\entity%
	mimic\radius#=this\radius#	
	mimic\blend%=this\blend%
	mimic\hudimagefilename$=this\hudimagefilename$
	mimic\hudimage%=this\hudimage%
	mimic\range#=this\range#
	mimic\traveltype%=this\traveltype%
	mimic\travelspeed#=this\travelspeed#
	mimic\travelpitchfactor#=this\travelpitchfactor#
	mimic\travelyawfactor#=this\travelyawfactor#
	mimic\travelrollfactor#=this\travelrollfactor#	
	mimic\travelanglefactor#=this\travelanglefactor#	
	mimic\mass#=this\mass#
	mimic\force#=this\force#
	mimic\bounce# =this\bounce# 
	mimic\friction#=this\friction#
	mimic\xvel# =this\xvel# 
	mimic\yvel# =this\yvel# 
	mimic\zvel#=this\zvel#
	mimic\energy%=this\energy%
	mimic\detonatetype%=this\detonatetype%	
	mimic\effect%=this\effect%
	mimic\effecttarget%=this\effecttarget%
	mimic\effectduration%=this\effectduration%
	mimic\effectarea%=this\effectarea%
	mimic\effectvalue%=this\effectvalue%
	mimic\effectdecay#=this\effectdecay#
End Function

Function projectileCreate%(projectileID%,projectileloadreactorID%,projectilefirereactorID%,projectiletravelreactorID%,projectileimpactreactorID%,projectileloadaudioID%,projectilefireaudioID%,projectiletravelaudioID%,projectileimpactaudioID%,projectilerecyclerID%,projectilefilename$,projectileentitytype%,projectileentity%,projectileradius#,projectilerange#,projectiletraveltype%,projectileeffect%,projectileeffecttarget%,projectileeffectduration%,projectileeffectarea%,projectileeffectvalue%,projectileeffectdecay#)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.projectile=projectileNew()
	this\ID%=projectileID%
	this\loadreactorID%=projectileloadreactorID%
	this\firereactorID%=projectilefirereactorID%
	this\travelreactorID%=projectiletravelreactorID%
	this\impactreactorID%=projectileimpactreactorID%
	this\debrisreactorID%=projectiledebrisreactorID%
	this\loadaudioID%=projectileloadaudioID%
	this\fireaudioID%=projectilefireaudioID%
	this\travelaudioID%=projectiletravelaudioID%
	this\impactaudioID%=projectileimpactaudioID%
	this\debrisaudioID%=projectiledebrisaudioID%
	this\recyclerID%=projectilerecyclerID%
	this\filename$=projectilefilename$
	this\EntityType%=projectileentitytype%
	this\entity%=projectileentity%
	this\radius%=projectileradius#
	this\blend%=0	
	this\hudimagefilename$=projectilehudimagefilename$
	this\hudimage%=projectilehudimage%	
	this\range#=projectilerange#
	this\traveltype%=projectiletraveltype%
	this\travelspeed#=projectiletravelspeed#
	this\travelpitchfactor#=projectiletravelpitchfactor
	this\travelyawfactor#=projectiletravelyawfactor
	this\travelrollfactor#=projectiletravelrollfactor	
	this\travelanglefactor#=projectiletravelanglefactor
	this\mass#=0.0
	this\force#=0.0
	this\bounce# =0.0
	this\friction#=0.0
	this\xvel# =0.0
	this\yvel# =0.0
	this\zvel#=0.0
	this\energy%=projectileenergy%
	this\detonatetype%=projectiledetonatetype%	
	this\effect%=projectileeffect%
	this\effecttarget%=projectileeffecttarget%
	this\effectduration%=projectileeffectduration%
	this\effectarea%=projectileeffectarea%
	this\effectvalue%=projectileeffectvalue%
	this\effectdecay#=projectileeffectdecay#
	Return this\id
End Function

Function projectileDestroy(projectileid%)
	;Purpose: Remove object by ID
	;Parameters: projectileid%=Object's ID
	;Return: None
	projectileDelete(projectile_ID[projectileid%])
End Function

Function projectileSet(this.projectile,projectileID%,projectileloadreactorID%,projectilefirereactorID%,projectiletravelreactorID%,projectileimpactreactorID%,projectileloadaudioID%,projectilefireaudioID%,projectiletravelaudioID%,projectileimpactaudioID%,projectilerecyclerID%,projectilefilename$,projectileentitytype%,projectileentity%,projectileradius#,projectilerange#,projectiletraveltype%,projectileeffect%,projectileeffecttarget%,projectileeffectduration%,projectileeffectarea%,projectileeffectvalue%,projectileeffectdecay#)
	;Purpose: Set projectile Property Values
	;Parameters: projectile Instance and Properties
	;Return: None
	this\ID%=projectileID%
	this\loadreactorID%=projectileloadreactorID%
	this\firereactorID%=projectilefirereactorID%
	this\travelreactorID%=projectiletravelreactorID%
	this\impactreactorID%=projectileimpactreactorID%
	this\debrisreactorID%=projectiledebrisreactorID%
	this\loadaudioID%=projectileloadaudioID%
	this\fireaudioID%=projectilefireaudioID%
	this\travelaudioID%=projectiletravelaudioID%
	this\impactaudioID%=projectileimpactaudioID%
	this\debrisaudioID%=projectiledebrisaudioID%
	this\recyclerID%=projectilerecyclerID%
	this\filename$=projectilefilename$
	this\EntityType%=projectileentitytype%
	this\entity%=projectileentity%
	this\radius#=projectileradius#	
	this\blend%=0
	this\hudimagefilename$=projectilehudimagefilename$
	this\hudimage%=projectilehudimage%		
	this\range#=projectilerange#
	this\traveltype%=projectiletraveltype%
	this\travelspeed#=projectiletravelspeed#
	this\travelpitchfactor#=projectiletravelpitchfactor
	this\travelyawfactor#=projectiletravelyawfactor
	this\travelrollfactor#=projectiletravelrollfactor
	this\travelanglefactor#=projectiletravelanglefactor
	this\mass#=0.0
	this\force#=0.0
	this\bounce# =0.0
	this\friction#=0.0
	this\xvel# =0.0
	this\yvel# =0.0
	this\zvel#=0.0
	this\energy%=projectileenergy%
	this\detonatetype%=projectiledetonatetype%	
	this\effect%=projectileeffect%
	this\effecttarget%=projectileeffecttarget%
	this\effectduration%=projectileeffectduration%
	this\effectarea%=projectileeffectarea%
	this\effectvalue%=projectileeffectvalue%
	this\effectdecay#=projectileeffectdecay#
End Function

Function projectileDefaultDatWrite()
	;Purpose: Creates default dat file
	;Parameters: TBD
	;Return: projectile Instance
	
	;1:plasma bit
	this.projectile=projectileNew()
	this\loadreactorID%=1
	this\firereactorID%=2
	this\travelreactorID%=3
	this\impactreactorID%=4
	this\debrisreactorID%=5
	this\loadaudioID%=0
	this\fireaudioID%=1
	this\travelaudioID%=0
	this\impactaudioID%=2
	this\debrisaudioID%=0
	this\name$="Plasma Bit"
	this\filename$="..\game\data\3d\weapons\plasmaorb.b3d";
	this\EntityType%=PROJECTILE_ENTITYTYPE_MESH
	this\radius#=.25	
	this\blend%=3
	this\hudimagefilename$="..\game\data\3d\weapons\plasmaorbhud.bmp"
	this\range#=24.0
	this\traveltype%= PROJECTILE_TRAVELTYPE_STRAIGHT
	this\travelspeed#=1.0
	this\travelrollfactor#=45.0	
	this\mass#=0.0
	this\force#=0.0
	this\bounce# =0.0
	this\friction#=0.0
	this\xvel# =0.0
	this\yvel# =0.0
	this\zvel#=0.0
	this\energy%=PROJECTILE_ENERGY_COSMIC
	this\detonatetype%=PROJECTILE_DETONATETYPE_IMPACT		
	this\effect%=0
	this\effecttarget%=0
	this\effectduration%=0
	this\effectarea%=0
	this\effectvalue%=0
	this\effectdecay#=0.0
	
	;2:red shocker orb
	this.projectile=projectileNew()
	this\loadreactorID%=1
	this\firereactorID%=6
	this\travelreactorID%=7
	this\impactreactorID%=8
	this\debrisreactorID%=9
	this\loadaudioID%=0
	this\fireaudioID%=6
	this\travelaudioID%=0
	this\impactaudioID%=8
	this\debrisaudioID%=0
	this\name$="Red Shocker Orb"
	this\filename$="..\game\data\3d\reactors\redspark.bmp"
	this\EntityType%=PROJECTILE_ENTITYTYPE_SPRITE
	this\radius#=.35	
	this\hudimagefilename$="..\game\data\3d\weapons\plasmaorbhud.bmp"
	this\range#=96.0
	this\blend%=3
	this\traveltype%= PROJECTILE_TRAVELTYPE_STRAIGHT
	this\travelspeed#=1
	this\travelanglefactor#=20.0
	this\mass#=0.0
	this\force#=0.0
	this\bounce# =0.0
	this\friction#=0.0
	this\xvel# =0.0
	this\yvel# =0.0
	this\zvel#=0.0
	this\energy%=PROJECTILE_ENERGY_ELECTRICAL
	this\detonatetype%=PROJECTILE_DETONATETYPE_IMPACT		
	this\effect%=0
	this\effecttarget%=0
	this\effectduration%=0
	this\effectarea%=0
	this\effectvalue%=0
	this\effectdecay#=0.0

	;3:plasma fragger
	this.projectile=projectileNew()
	this\loadreactorID%=1
	this\firereactorID%=10
	this\travelreactorID%=10
	this\impactreactorID%=11
	this\debrisreactorID%=9
	this\loadaudioID%=0
	this\fireaudioID%=6
	this\travelaudioID%=0
	this\impactaudioID%=2
	this\debrisaudioID%=0
	this\name$="Plasma Fragger"
	this\filename$="..\game\data\3d\weapons\grenade.b3d"
	this\EntityType%=PROJECTILE_ENTITYTYPE_MESH
	this\radius#=.1	
	this\blend%=0
	this\hudimagefilename$="..\game\data\3d\weapons\plasmaorbhud.bmp"
	this\range#=96.0
	this\traveltype%= PROJECTILE_TRAVELTYPE_LOB
	this\travelspeed#=.3
	this\travelanglefactor#=0.0
	this\mass#=0.0
	this\force#=-2.0
	this\bounce# =0.9
	this\friction#=.98
	this\xvel# =0.0
	this\yvel# =0.0
	this\zvel#=0.0
	this\energy%=PROJECTILE_ENERGY_METAL
	this\detonatetype%=PROJECTILE_DETONATETYPE_TIMEOUT		
	this\effect%=0
	this\effecttarget%=0
	this\effectduration%=0
	this\effectarea%=0
	this\effectvalue%=0
	this\effectdecay#=0.0
	
	projectileSave()
End Function

Function projectileMethod.projectile(this.projectile)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

If gamedatawrite projectileDefaultDatWrite