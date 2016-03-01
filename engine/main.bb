;PLASMA FPS by Casey Kerr, Frankie Taylor, Steven Campbell
;all rights reserved (C) 2005

.PLASMA_INCLUDE
	Global gamedatawrite=False
	Global weapontest=False
	Global networktest=False
	Global bullettime	
	Global markerCTR
	;Dim marker(1000);physics testing
	
	Include "common\common.bb"	
	Include "console\console.bb"
	Include "game\game.bb"
	Include "game\editor.bb"
	Include "network\network.bb"
	Include "map\map.bb"
	Include "map\item.bb"
	Include "map\spawner.bb"
	Include "audio\audio.bb"
	Include "audio\sound.bb"
	Include "reactor\recycler.bb"
	Include "reactor\reactor.bb"
	Include "reactor\light.bb"
	Include "reactor\particle.bb"
	Include "weapon\weapon.bb"
	Include "weapon\gun.bb"
	Include "weapon\projectile.bb"
	Include "weapon\ammo.bb"
	Include "weapon\physics.bb"
	Include "player\character.test.bb"
	Include "player\camera.bb"
	Include "player\player.bb"
	Include "player\controlmap.bb"
	Include "player\imagemap.bb"

.PLASMA_START
	commonstart()
	networkstart()
	consolestart()
	gameStart()
	mapStart(game\rootdirectory$+"..\game\data\dat\map.dat")
	recyclerStart()
	reactorStart(game\rootdirectory$+"..\game\data\dat\reactor.dat")
	lightStart()
	particleStart()
	weaponStart(game\rootdirectory$+"..\game\data\dat\weapon.dat")
	gunStart()
	projectileStart(game\rootdirectory$+"..\game\data\dat\projectile.dat")
	ammoStart()	
	physicsStart()
	audiostart(game\rootdirectory$+"..\game\data\dat\audio.dat")
	soundStart()	
	characterStart(game\rootdirectory$+"..\game\data\dat\character.dat")
	controlmapStart(game\rootdirectory$+"..\game\Data\dat\controlmap.dat")
	cameraStart()
	playerStart()
	imagemapStart(game\rootdirectory$+"..\game\Data\dat\imagemap.dat")
	

	font=LoadFont ("arial",12,True)
	SetFont(font)

	game_State=GAME_STATE_START

	Global PLAYERHIT;player collision testing

	;marker(0)=CreateSphere(8)
	;ScaleMesh(marker(0),.05,.05,.05)
	;RotateMesh(marker(0),90,0,0)
	;HideEntity(marker(0))
	
.PLASMA_UPDATE ;main loop
	While game_State<>GAME_STATE_EXIT
		
		Cls()
		

		If game_State = GAME_STATE_PLAYING
			player.player=Player_ID[PlayerControlling()]
			If player.player<>Null
				;controlmap.controlmap=controlmap_ID[player_ID[1]\controlmapID]
				controlmap.controlmap=First controlmap
				gun1.gun=player\gun[1]
				gun2.gun=player\gun[2]

				weapontest=True
			End If
			
			If weapontest
					;3D
					;weapon alignment testing
					If gun1\weaponID And gun2\weaponID
						If KeyDown(200) :MoveEntity(gun1\entity,0,0,.1) :MoveEntity(gun2\entity,0,0,.1) :EndIf
						If KeyDown(208) :MoveEntity(gun1\entity,0,0,-.1) :MoveEntity(gun2\entity,0,0,-.1) :EndIf
						If KeyDown(203) :MoveEntity(gun1\entity,-.1,0,0) :MoveEntity(gun2\entity,.1,0,0) :EndIf
						If KeyDown(205) :MoveEntity(gun1\entity,.1,0,0) :MoveEntity(gun2\entity,-.1,0,0) :EndIf
						If KeyDown(201) :MoveEntity(gun1\entity,0,.1,0) :MoveEntity(gun2\entity,0,.1,0) :EndIf
						If KeyDown(209) :MoveEntity(gun1\entity,0,-.1,0) :MoveEntity(gun2\entity,0,-.1,0) :EndIf
					EndIf
			
					;weapon mount/unmount testing
					If controlmapGetInput(controlmap\gunselect1,CONTROLMAP_PRESS_HIT) ;lctrl
						If gun1\weaponID gunUnmount(gun1)
						lgun=lgun+1
						If lgun>3 lgun=1
						gunSet(gun1,lgun,recyclerPop(recycler_ID[weapon_ID[lgun]\recyclerID%]),GUN_STATE_IDLE)				
			 			gunMount(gun1,player,GUN_GRIP_LEFT)
					EndIf
					
					If controlmapGetInput(controlmap\gunselect2,CONTROLMAP_PRESS_HIT)
						If gun2\weaponID gunUnmount(gun2)
						rgun=rgun+1
						If rgun>3 rgun=1
						gunSet(gun2,rgun,recyclerPop(recycler_ID[weapon_ID[rgun]\recyclerID%]),GUN_STATE_IDLE)				
			 			gunMount(gun2,player,GUN_GRIP_RIGHT)
					EndIf			
					
					If gun1\WeaponID
						;rapid fire testing (rshift)
						If KeyHit(54): rpulse=16: weapon_ID[1]\trigger=3: EndIf: If rpulse:	rpulse=rpulse-1: If rpulse=0: weapon_ID[1]\trigger=2: EndIf: EndIf
						If controlmapGetInput(controlmap\gununmount1) And KeyDown(42)=falsue gunUnmount(gun1)
						If controlmapGetInput(controlmap\gununmount1) And KeyDown(42)=True EntityParent(gun1\entity,0)
						If KeyHit(2) gun1\state=GUN_STATE_RELOAD
					EndIf
					
					If gun2\WeaponID	
						If controlmapGetInput(controlmap\gununmount2) And KeyDown(42)=falsue gunUnmount(gun2)
						If controlmapGetInput(controlmap\gununmount2) And KeyDown(42)=True EntityParent(gun2\entity,0)
						If KeyHit(3) gun2\state=GUN_STATE_RELOAD
					EndIf
					
					If controlmapGetInput(controlmap\jump) MoveEntity(player\entity,0,.4,0)
					If controlmapGetInput(controlmap\escape) game_State=GAME_STATE_PAUSE			
			EndIf
			
			;debug pause
			If KeyHit(64) Delay(2000)
								
			;screenshot			
			If KeyHit(67) SaveBuffer(FrontBuffer(),"screenshot.bmp") ;F9
			
			;bullettime ammo view testing
			If KeyHit(20) bullettime=250 
			
			;exit application
			If KeyHit(1) Then game_State=GAME_STATE_EXIT

			
			mapUpdate()
			playerUpdate()
			cameraUpdate()
			If Not bullettime
				gunUpdate()
				ammoUpdate()
				physicsUpdate()
				lightUpdate()
				particleUpdate()
				soundUpdate()
			Else
				bullettime=bullettime-1
			EndIf 
			UpdateWorld()
			RenderWorld()
			If Not bullettime physicsUpdateWorld()

			If weapontest			
				;2D
				Color(0,255,0)
				Text(game\screenwidth/1.5,12*0,"Bullet[T]ime: "+bullettime+" RPulse: "+rpulse+" OCharge: "+ocharge)
				If gun1\weaponID
					Color(255,255,0)
					Text(game\screenwidth/1.5,12*1,"Weapon: "+weapon_ID[gun1\weaponID]\name$+"/"+projectile_ID[weapon_ID[gun1\weaponID]\projectileID]\name$)
					Text(game\screenwidth/1.5,12*2,"Ammometer[1]: "+gun1\projectilesCTR%)
					Text(game\screenwidth/1.5,12*3,"Charging: "+gun1\chargeupCTR#)	
					Text(game\screenwidth/1.5,12*4,"Cooling: "+gun1\coolingCTR#)	
					Text(game\screenwidth/1.5,12*5,"State: "+gun1\state%)
					Text(game\screenwidth/1.5,12*6,"Trigger: "+weapon_ID[1]\trigger+" TriggerState: "+gun1\triggerstate%+":"+gun1\triggeroldstate)
					Text(game\screenwidth/1.5,12*7,"LGrip: "+EntityX(gun1\entity)+","+EntityY(gun1\entity)+","+EntityZ(gun1\entity))
				EndIf
				If gun2\weaponID
					Color(255,127,63)
					Text(game\screenwidth/1.5,12*8,"Weapon: "+weapon_ID[gun2\weaponID]\name$+"/"+projectile_ID[weapon_ID[gun2\weaponID]\projectileID]\name$)
					Text(game\screenwidth/1.5,12*9,"Ammometer[2]: "+gun2\projectilesCTR%)
					Text(game\screenwidth/1.5,12*10,"Charging: "+gun2\chargeupCTR)	
					Text(game\screenwidth/1.5,12*11,"Cooling: "+gun2\coolingCTR#)	
					Text(game\screenwidth/1.5,12*12,"State: "+gun2\state)
					Text(game\screenwidth/1.5,12*13,"Trigger: "+gun2\triggerstate%+":"+gun2\triggeroldstate)
					Text(game\screenwidth/1.5,12*14,"RGrip: "+EntityX(gun2\entity)+","+EntityY(gun2\entity)+","+EntityZ(gun2\entity))
				EndIf
			EndIf			
		
		EndIf ;game_State
		imagemapUpdate()
		gameUpdate()
		ConsoleUpdate()
		networkUpdate()
		CommonUpdate()
		Text(game\screenwidth/2,10,"Ping:"+Client_PingTime%+" ms",1,1)
		Text(game\screenwidth/2,20,"IN :"+BANK_DataIn%+" bps",1,1)
		Text(game\screenwidth/2,30,"OUT:"+BANK_DataOut%+" bps",1,1)
		Flip(True)
	Wend
	
.PLASMA_STOP
	mapStop()
	characterStop()
	playerStop()
	cameraStop()
	controlmapStop()
	weaponStop()
	gunStop()
	projectileStop()
	ammoStop()
	reactorStop()
	particleStop()
	recyclerStop()
	imagemapStop()
	consolestop()
	networkstop()
	CommonStop()
End