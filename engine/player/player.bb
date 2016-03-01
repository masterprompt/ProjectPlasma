;============================
;PLAYER CLASS
;Generated with TypeWriterIV
;Controls each Player in a game
;	-Controls are mapped to player only if player is client player
;	-Camera is mapped to player only if player is client player
;============================

;============================
;INCLUDES
;============================
;Include "character.test.bb"
;Include "controlmap.bb"
;Include "camera.bb"
;Include "../reactor/recycler.bb"
;Include "../weapon/weapon.bb"
;Include "../weapon/gun.bb"
;Include "../weapon/projectile.bb"
;Include "../weapon/ammo.bb"

;============================
;CONSTANTS
;============================
Const PLAYER_MAX%=16
Const PLAYER_STATE_DEAD=0
Const PLAYER_STATE_IDLE=1

;Net Properties
Const NET_PLAYER_ID			=1
Const NET_PLAYER_NAME			=2
Const NET_PLAYER_CHARACTERID		=3
Const NET_PLAYER_CONTROLMAPID		=4
Const NET_PLAYER_ENTITY			=5
Const NET_PLAYER_CAMERAPIVOT		=6
Const NET_PLAYER_STATE			=7
Const NET_PLAYER_GENERATEMODE		=8
Const NET_PLAYER_PING			=9

Const NET_PLAYER_METHOD_CREATE		=1
CONST NET_PLAYER_METHOD_DESTROY		=2
CONST NET_PLAYER_METHOD_UPDATEPOS	=3

;============================
;GLOBALS
;============================
Global player_ID.player[PLAYER_MAX%];Primary Key Object Pointer
Global player_Index%[PLAYER_MAX%], player_IndexPointer%;Primary Key ID Management Stack
Global player_SightPivot%; global pivot shared by all guns

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type player
	;Purpose:
	;Properties:
	Field ID%;
	Field name$;
	Field ClientID;
	Field characterID%;
	Field controlmapID;
	Field camera.camera;
	Field gun.gun[2];
	Field entity%;
	Field camerapivot%
	Field state%;
	Field GenerateMode%;
	Field Ping%;
	Field Score%[5];
End Type

;============================
;METHODS
;============================
Function playerStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For playerloop=PLAYER_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		playerIndexPush(playerloop)
	Next
	;playerCreate%(1,1)
End Function

Function playerStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.player=Each player
		playerDelete(this)
	Next
End Function

Function playerNew.player(DesiredID=0)
	;Purpose: CONSTRUCTOR - Creates player Instance; Manual or Auto ID generation
	;Parameters: DesiredID  0=Auto, >0 = Manual
	;Return: player Instance
	if player_ID[DesiredID] = null and DesiredID >0 then;Manual ID Creation Doesn't exist
		this.player=New player
		this\ID%=0
		this\characterID%=0
		this\controlmapID%=0
		this\camera.Camera = Cameranew()
		For playerloop=1 To 2
			this\gun.gun[playerloop]=gunNew()
		Next
		this\entity%=0
		this\state%=1
		this\ClientID=0
		this\ID%=DesiredID
		this\GenerateMode%=0
		player_ID[this\ID%]=this
	Elseif player_ID[DesiredID] <> null and DesiredID >0 then;Manual ID Creation Exists
		this.player = player_ID[DesiredID]
		this\ClientID=0
	Else;Auto ID Creation
		this.player=New player
		this\ID%=0
		this\characterID%=0
		this\controlmapID%=0
		this\camera.Camera = Cameranew()
		For playerloop=1 To 2
			this\gun.gun[playerloop]=gunNew()
		Next
		this\entity%=0
		this\state%=1
		this\ClientID=0
		this\GenerateMode%=1
		this\ID%=playerIndexPop()
		player_ID[this\ID%]=this
	Endif
	Return this
End Function

Function playerDelete(this.player)
	;Purpose: DESTRUCTOR - Removes player Instance
	;Parameters: player Instance
	;Return: None
	player_ID[this\ID%]=Null
	if this\characterID <> 0 then FreeEntity(this\characterID):this\characterID=0
	if this\GenerateMode% then playerIndexPush(this\ID%)
	For playerloop=1 To 2
		gunDelete(this\gun[playerloop])
	Next
	if this\camera <> null then cameraDelete(this\camera.camera)
	If this\entity% <> 0 Then FreeEntity(this\entity%)
	playerIndexPush(this\ID%)
	Delete this
End Function

Function playerUpdate()
	;Purpose: Main Loop Update. Updates all player Instances.
	;Parameters: None
	;Return: None
	For this.player=Each player
		If this\state;Is this player under our control?
			If this\controlmapID%
				playerHudDisplay(this)
				playerControl(this)
				;Gravity
				MoveEntity(this\entity,0,-.1,0)
			Else ;network controlled player
				;networkPlayerControl(this)
			EndIf
		EndIf
	Next
End Function

Function playerWrite(this.player,playerfile)
	;Purpose: Writes player Property Values to File
	;Parameters: player Instance, playerfile=filehandle
	;Return: None
	WriteInt(playerfile,this\ID%)
	WriteInt(playerfile,this\characterID%)
	WriteInt(playerfile,this\controlmapID%)
	WriteString(playerfile,this\name$)
	cameraWrite(this\camera.camera,playerfile)
	For playerloop=1 To 2
		gunWrite(this\gun.gun[playerloop],playerfile)
	Next
	WriteInt(playerfile,this\entity%)
	WriteInt(playerfile,this\state%)
End Function

Function playerRead.player(playerfile)
	;Purpose: Reads File To player Property Values
	;Parameters: playerfile=filehandle
	;Return: player Instance
	this.player=New player
	this\ID%=ReadInt(playerfile)
	this\characterID%=ReadInt(playerfile)
	this\controlmapID%=ReadInt(playerfile)
	this\name$=ReadString(playerfile)
	this\camera.camera=cameraRead(playerfile)
	For playerloop=1 To 2
		this\gun.gun[playerloop]=gunRead(playerfile)
	Next
	this\entity%=ReadInt(playerfile)
	this\state%=ReadInt(playerfile)
	playerIndexPop()
	player_ID[this\ID%]=this
	Return this
End Function

Function playerSave(playerfilename$="player.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: playerfilename$= Name of File
	;Return: None
	playerfile=WriteFile(playerfilename$)
	For this.player= Each player
		playerWrite(this,playerfile)
	Next
	CloseFile(playerfile)
End Function

Function playerOpen(playerfilename$="player.dat")
	;Purpose: Opens a Object Property file
	;Parameters: playerfilename$=Name of Property File
	;Return: None
	playerfile=ReadFile(playerfilename$)
	Repeat
		playerRead(playerfile)
	Until Eof(playerfile)
	CloseFile(playerfile)
End Function

Function playerCopy.player(this.player)
	;Purpose: Creates a Copy of a player Instance
	;Parameters: player Instance
	;Return: Copy of player Instance
	copy.player=playerNew()
	copy\ID%=this\ID%
	copy\characterID%=this\characterID%
	copy\controlmapID%=this\controlmapID%
	copy\name$=this\name$
	copy\camera.camera=cameraCopy(this\camera.camera)
	For playerloop=1 To 2
		copy\gun.gun[playerloop]=gunCopy(this\gun[playerloop])
	Next
	copy\entity%=this\entity%
	copy\state%=this\state%
	Return copy
End Function

Function playerMimic(this.player,mimic.player)
	;Purpose: Copies Property Values from one player Instance To another
	;Parameters: player Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\characterID%=this\characterID%
	mimic\controlmapID%=this\controlmapID%
	mimic\name$=this\name$
	cameraMimic(this\camera.camera,mimic\camera.camera)
	For playerloop=1 To 2
		gunMimic(this\gun[playerloop],mimic\gun[playerloop])
	Next
	mimic\entity%=this\entity%
	mimic\state%=this\state%
End Function

Function playerCreate.Player(Desired=0, ThisName$="")
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.player=playerNew(Desired)
	;set player character profile object key
	;this\characterID%=playercharacterID%
	;this\controlmapID%=1
	;Create a basic player shape for our control
	This\Name$ =ThisName$
	if this\entity%=0 then 
		this\entity%=Createpivot()
		EntityType(this\entity%,GAME_COLLISIONTYPE_PLAYER)
		EntityRadius(this\entity%,1)
	
		;setup camera
		this\camerapivot=CreatePivot(this\entity)
		this\characterID%=createcube(this\entity)
		R=rnd(0,255)
		G=rnd(0,255)
		B=rnd(0,255)
		entitycolor(this\characterID,R,G,B)
		;cameraSet(this\camera,CreatePivot(),0,90,-90,CreateCamera())
		;CreateListener(this\camera\vpivot%);this should parent to camera, a kludge
		EntityParent(this\camera\vpivot,this\camerapivot%)
		;adjust camera pivot to raise the camera up
		PositionEntity(this\camerapivot,0,1,0)
		;create player_SightPivot% for local player only
		player_SightPivot%=CreateSprite(this\camera\entity%)
		EntityOrder(player_SightPivot%,-100); sight always on top of screen
		EntityBlend(player_SightPivot%,3)
		;Set state to active
	endif
	this\state%=PLAYER_STATE_IDLE%
	Return this
End Function

Function playerDestroy(playerid%)
	;Purpose: Remove object by ID
	;Parameters: playerid%=Object's ID
	;Return: None
	playerDelete(player_ID[playerid%])
End Function

Function playerSet(this.player,playerID%,playercharacterID%,playercontrolmapID%,playercontrolmap.controlmap,playercamera.camera,gun1.gun,gun2.gun,playerentity%,playerstate%)
	;Purpose: Set player Property Values
	;Parameters: player Instance and Properties
	;Return: None
	this\ID%=playerID%
	this\characterID%=playercharacterID%
	this\controlmapID%=playercontrolmapID%
	this\camera.camera=playercamera.camera
	this\gun.gun[1]=gun1.gun
	this\gun.gun[2]=gun2.gun
	this\entity%=playerentity%
	this\state%=playerstate%
End Function

Function playerIndexPush(playerid%)
	;Purpose: Pushes ID into Primary Key ID Management Stack
	;Parameters: playerid=Object's ID
	;Return: None
	player_Index[player_IndexPointer]=playerid%
	player_IndexPointer=player_IndexPointer+1
End Function

Function playerIndexPop()
	;Purpose: Pops ID out of Primary Key ID Management Stack
	;Parameters: None
	;Return: Object ID%
	player_IndexPointer=player_IndexPointer-1
	Return player_Index[player_IndexPointer]
End Function

Function playerSetControl(PlayerID, playercontrolmapid%=1)
	;Purpose: Activates This Player for our control
	;Parameters: player ID and Properties
	;Return: None
	This.Player = player_ID[PlayerID]
	this\controlmapID%=playercontrolmapid%
End Function

Function playerSetView(PlayerID, playercamerastate%=0)
	;Purpose: Activates View of player
	;Parameters: player ID and Properties
	;Return: None
	This.Player = player_ID[PlayerID]
	cameraSetState(this\camera,playercamerastate%)
	If this\camera\state
		HideEntity(this\characterID%)
	Else
		ShowEntity(this\characterID%)
	EndIf
End Function

Function playerDatSave(this.player,playerfilename$="player.dat")
	;Purpose: Saves single player Object Properties to a file
	;Parameters: playerfilename$= Name of File
	;Return: None
	playerfile=WriteFile(playerfilename$)
	playerWrite(this,playerfile)
	CloseFile(playerfile)
End Function

Function playerHudDisplay(this.player)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
End Function

Function PlayerUpdatePos(this.player, ThisBank.Bank)
	if this\controlmapID% = 0 then
		ThisX# = BankReadFloat(ThisBank)
		ThisY# = BankReadFloat(ThisBank)
		ThisZ# = BankReadFloat(ThisBank)
		positionentity(this\entity,ThisX#,ThisY#,ThisZ#)
	endif
End Function

Function playerControl(this.player)
	;Purpose: Controls Player with Control Map only applies to local player
	;Parameters: player instance
	;Return: none
	if this\controlmapID% <> 0 then
		playerSetView(this\ID, 1)
		controlmap.controlmap=controlmap_ID[this\controlmapID%]
		;Do MouseLook
		this\camera\pitch#=this\camera\pitch#+controlmapGetInput(controlmap\mouselookx%);adjust camera pitch
		TurnEntity(this\entity,0,-controlmapGetInput(controlmap\mouselooky%),0);turn player body

		;Do Movement
		If controlmapGetInput(controlmap\forward) Then MoveEntity(this\entity,0,0,.25)
		If controlmapGetInput(controlmap\backward) Then MoveEntity(this\entity,0,0,-.25)
		If controlmapGetInput(controlmap\strafeleft) Then MoveEntity(this\entity,-.25,0,0)
		If controlmapGetInput(controlmap\straferight) Then MoveEntity(this\entity,.25,0,0)
		If controlmapGetInput(controlmap\jump) MoveEntity(this\entity,0,.4,0)
		;Do Weapons
	
		If this\gun[1]\weaponID%>0 And controlmapGetInput(controlmap\gunfire1%)  this\gun[1]\triggerstate=GUN_TRIGGER_STATE_DOWN
		If this\gun[2]\weaponID%>0 And controlmapGetInput(controlmap\gunfire2%)  this\gun[2]\triggerstate=GUN_TRIGGER_STATE_DOWN
		;Do
		If controlmapGetInput(controlmap\escape) Then gamestate=GAME_STATE_EXIT;PAUSE
	endif
End Function

Function PlayerControlling()
	;Purpose: Checks wich player this app is controlling
	;Parameters: None
	;Return: Player ID
	For This.Player = each Player
		if this\controlmapID% <> 0 then return(this\ID)
	Next
End Function

Function PlayerNetSet(ThisBank.Bank)
	ThisGroup 	= BankReadbyte(ThisBank)
	ThisSubObject	= BankReadbyte(ThisBank)
	ThisProperty 	= BankReadbyte(ThisBank)
	This.player = Player_ID[ThisGroup]
	Select ThisProperty
		Case NET_PLAYER_ID			this\ID%		=BankReadByte(ThisBank)
		Case NET_PLAYER_NAME			this\NAME$		=BankReadByte(ThisBank)
		Case NET_PLAYER_CHARACTERID		this\CHARACTERID%	=BankReadByte(ThisBank)
		Case NET_PLAYER_CONTROLMAPID		this\CONTROLMAPID%	=BankReadByte(ThisBank)
		Case NET_PLAYER_ENTITY			this\ENTITY%		=BankReadint(ThisBank)
		Case NET_PLAYER_CAMERAPIVOT		this\CAMERAPIVOT%	=BankReadint(ThisBank)
		Case NET_PLAYER_STATE			this\STATE%		=BankReadByte(ThisBank)
		Case NET_PLAYER_GENERATEMODE		this\GENERATEMODE%	=BankReadByte(ThisBank)
		Case NET_PLAYER_PING			this\PING%		=BankReadshort(ThisBank)
	End Select
End Function

Function PlayerNetRun(ThisBank.Bank)
	ThisGroup 	= BankReadbyte(ThisBank)
	ThisMethod 	= BankReadbyte(ThisBank)
	ThisProperty 	= BankReadbyte(ThisBank)
	if ThisGroup <> 0 then This.player = Player_ID[ThisGroup]
	Select ThisMethod
		Case NET_PLAYER_METHOD_CREATE		playerCreate(BankReadByte(ThisBank))
		Case NET_PLAYER_METHOD_DESTROY		playerDestroy(BankReadByte(ThisBank))
		case NET_PLAYER_METHOD_UPDATEPOS	PlayerUpdatePos(this.player, ThisBank.Bank)
	End Select
End Function

Function playerDefaultDatWrite()
	;Purpose: CONSTRUCTOR - Creates player Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: player Instance
	For playerloop=PLAYER_MAX To 1 Step -1; Initialize Primary Key ID Management Stack
		playerIndexPush(playerloop)
	Next
	this.player=playerNew()
	this\ID%=1
	this\characterID%=1
	this\state%=PLAYER_STATE_ALIVE%
	playerSave()
End Function

Function playerMethod.player(this.player)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
