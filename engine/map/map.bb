;============================
;MAP CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const MAP_MAX%=1

;============================
;GLOBALS
;============================
Global map_ID.map[MAP_MAX%];Primary Key Object Pointer
Global map_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type map
	;Purpose: 
	;Properties:
	Field ID%;
	Field filename$;
	Field entity%;
	Field terrainheightfile$;
	Field terraincolorfile$
	Field terrain%;
	Field skymapfile$;
	Field sky%;
End Type

;============================
;METHODS
;============================
Function mapStart(mapfilename$="map.dat")
	;Purpose: Initialize Class
	;Parameters: mapfilename$ - the filename of the map data file
	;Return: None
	mapOpen(mapfilename$)
	map_ID[1]\entity%=LoadAnimMesh(map_ID[1]\filename%)
	mapHierarchyCollision(map_ID[1],map_ID[1]\entity%,GAME_COLLISIONTYPE_LEVEL)
	RotateEntity(CreateLight(),90,0,0) 
	;AmbientLight(255,255,255)
End Function

Function mapStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.map=Each map
		mapDelete(this)
	Next
End Function

Function mapNew.map()
	;Purpose: CONSTRUCTOR - Creates map Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: map Instance
	this.map=New map
	this\ID%=0
	this\filename$=""
	this\entity%=0
	this\terrainheightfile$=""
	this\terraincolorfile$=""
	this\terrain%=0
	this\skymapfile$=""
	this\sky%=0
	map_Index=map_Index+1
	this\id%=map_Index
	map_ID[this\id%]=this
	Return this
End Function

Function mapDelete(this.map)
	;Purpose: DESTRUCTOR - Removes map Instance
	;Parameters: map Instance
	;Return: None
	this\skymapfile$=""
	this\terrainheightfile$=""
	FreeEntity this\entity%
	this\filename$=""
	Delete this
End Function

Function mapUpdate()
	;Purpose: Main Loop Update. Updates all map Instances.
	;Parameters: None
	;Return: None
	For this.map=Each map
		;Gravity?
	Next
End Function

Function mapWrite(this.map,mapfile)
	;Purpose: Writes map Property Values to File
	;Parameters: map Instance, mapfile=filehandle
	;Return: None
	WriteInt(mapfile,this\ID%)
	WriteString(mapfile,this\filename$)
	WriteInt(mapfile,this\entity%)
	WriteString(mapfile,this\terrainheightfile$)
	WriteString(mapfile,this\terraincolorfile$)
	WriteInt(mapfile,this\terrain%)
	WriteString(mapfile,this\skymapfile$)
	WriteInt(mapfile,this\sky%)
End Function

Function mapRead.map(mapfile)
	;Purpose: Reads File To map Property Values
	;Parameters: mapfile=filehandle
	;Return: map Instance
	this.map=New map
	this\ID%=ReadInt(mapfile)
	this\filename$=ReadString(mapfile)
	this\entity%=ReadInt(mapfile)
	this\terrainheightfile$=ReadString(mapfile)
	this\terraincolorfile$=ReadString(mapfile)
	this\terrain%=ReadInt(mapfile)
	this\skymapfile$=ReadString(mapfile)
	this\sky%=ReadInt(mapfile)
	map_ID[this\id%]=this
	Return this
End Function

Function mapSave(mapfilename$="map.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: mapfilename$= Name of File
	;Return: None
	mapfile=WriteFile(mapfilename$)
	For this.map= Each map
		mapWrite(this,mapfile)
	Next
	
	CloseFile(mapfile)
End Function

Function mapOpen(mapfilename$="map.dat")
	;Purpose: Opens a Object Property file
	;Parameters: mapfilename$=Name of Property File
	;Return: None
	mapfile=ReadFile(mapfilename$)
	Repeat
		mapRead(mapfile)
	Until Eof(mapfile)
	CloseFile(mapfile)
End Function

Function mapCopy.map(this.map)
	;Purpose: Creates a Copy of a map Instance
	;Parameters: map Instance
	;Return: Copy of map Instance
	copy.map=mapNew()
	copy\ID%=this\ID%
	copy\filename$=this\filename$
	copy\entity%=this\entity%
	copy\terrainheightfile$=this\terrainheightfile$
	copy\terraincolorfile$=this\terrainheightfile$
	copy\terrain%=this\terrain%
	copy\skymapfile$=this\skymapfile$
	copy\sky%=this\sky%
	Return copy
End Function

Function mapMimic(this.map,mimic.map)
	;Purpose: Copies Property Values from one map Instance To another
	;Parameters: map Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\filename$=this\filename$
	mimic\entity%=this\entity%
	mimic\terrainheightfile$=this\terrainheightfile$
	mimic\terraincolorfile$=this\terraincolorfile$
	mimic\terrain%=this\terrain%
	mimic\skymapfile$=this\skymapfile$
	mimic\sky%=this\sky%
End Function

Function mapCreate%(mapID%,mapfilename$,mapentity%,mapterrainheightfile$, mapterraincolorfile$,mapterrain%,mapskymapfile$,mapsky%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.map=mapNew()
	this\ID%=mapID%
	this\filename$=mapfilename$
	this\entity%=mapentity%
	this\terrainheightfile$=mapterrainheightfile$
	this\terraincolorfile$=mapterraincolorfile$
	this\terrain%=mapterrain%
	this\skymapfile$=mapskymapfile$
	this\sky%=mapsky%
	Return this\id
End Function

Function mapDestroy(mapid%)
	;Purpose: Remove object by ID
	;Parameters: mapid%=Object's ID
	;Return: None
	mapDelete(map_ID[mapid%])
End Function

Function mapSet(this.map,mapID%,mapfilename$,mapentity%,mapterrainheightfile$,mapterrain%,mapskymapfile$,mapsky%)
	;Purpose: Set map Property Values
	;Parameters: map Instance and Properties
	;Return: None
	this\ID%=mapID%
	this\filename$=mapfilename$
	this\entity%=mapentity%
	this\terrainheightfile$=mapterrainheightfile$
	this\terrain%=mapterrain%
	this\skymapfile$=mapskymapfile$
	this\sky%=mapsky%
End Function

Function mapHierarchyCollision(this.map,mapparententity%,mapcollisiontype%)
	mapchildren%=CountChildren(mapparententity%) 
	For maploop = 1 To mapchildren% 
		mapchild%=GetChild(mapparententity%,maploop) 
		If mapchild%
				EntityType(mapchild%,mapcollisiontype%)
				;DebugLog(Str(mapchild%)+"/"+EntityName(mapchild%)+" Surfaces="+CountSurfaces(mapchild%));testing
		EndIf	
		mapHierarchyCollision(this,mapchild%,mapcollisiontype%) 
	Next 
End Function

Function mapDefaultDatWrite()
	;Purpose: Creates default map dat file. 
	;Parameters: TBD
	;Return: map Instance
	this.map=mapNew()
	this\filename$="..\game\data\3d\maps\level1.b3d"
	this\entity%=0
	this\terrainheightfile$=""
	this\terrain%=0
	this\skymapfile$=""
	this\sky%=0
	mapSave()
End Function

Function mapMethod.map(this.map)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

If gamedatawrite mapDefaultDatWrite