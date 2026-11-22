extends Node3D
var rng=RandomNumberGenerator.new()
var ball:Node3D
var keeper:Node3D
var shooter:Node3D
var cam:Camera3D
var score=0
var shots=0
var club=0
var active=false
var t=0.0
var aim=0.0
var power=0.65
var dest=Vector3.ZERO
var keeper_x=0.0
var goal=false
var info:Label
var title:Label
var menu:Control
const CLUBS=["CORINTHIANS","FLAMENGO","PALMEIRAS","SAO PAULO","SANTOS","VASCO","BAHIA","VITORIA","REAL MADRID","BARCELONA","LIVERPOOL","ARSENAL","MAN CITY","BAYERN","PSG","JUVENTUS"]
const COLORS=[Color.WHITE,Color.RED,Color.DARK_GREEN,Color.WHITE,Color.WHITE,Color.BLACK,Color.BLUE,Color.RED,Color.WHITE,Color.DARK_RED,Color.RED,Color.RED,Color.SKY_BLUE,Color.RED,Color.BLUE,Color.WHITE]
func box(parent:Node3D,size:Vector3,pos:Vector3,color:Color)->MeshInstance3D:
 var n=MeshInstance3D.new()
 var m=BoxMesh.new()
 m.size=size
 n.mesh=m
 var mat=StandardMaterial3D.new()
 mat.albedo_color=color
 n.material_override=mat
 n.position=pos
 parent.add_child(n)
 return n
func sphere(parent:Node3D,r:float,pos:Vector3,color:Color)->void:
 var n=MeshInstance3D.new()
 var m=SphereMesh.new()
 m.radius=r
 m.height=2*r
 n.mesh=m
 var mat=StandardMaterial3D.new()
 mat.albedo_color=color
 n.material_override=mat
 n.position=pos
 parent.add_child(n)
func human(parent:Node3D,color:Color)->void:
 box(parent,Vector3(0.65,0.85,0.34),Vector3(0,1.32,0),color)
 sphere(parent,0.23,Vector3(0,2.05,0),Color(0.65,0.44,0.3))
 for side in [-1.0,1.0]:
  box(parent,Vector3(0.17,0.7,0.17),Vector3(side*0.44,1.3,0),Color(0.65,0.44,0.3))
  box(parent,Vector3(0.22,0.84,0.24),Vector3(side*0.2,0.48,0),Color(0.13,0.16,0.2))
  box(parent,Vector3(0.3,0.13,0.48),Vector3(side*0.2,0.08,-0.12),Color(0.07,0.07,0.08))
func _ready()->void:
 rng.randomize()
 var env=WorldEnvironment.new()
 var e=Environment.new()
 e.background_mode=Environment.BG_COLOR
 e.background_color=Color(0.01,0.025,0.055)
 e.ambient_light_source=Environment.AMBIENT_SOURCE_COLOR
 e.ambient_light_color=Color(0.7,0.8,1)
 env.environment=e
 add_child(env)
 var sun=DirectionalLight3D.new()
 sun.rotation_degrees=Vector3(-55,-20,0)
 sun.light_energy=1.2
 add_child(sun)
 cam=Camera3D.new()
 cam.position=Vector3(0,4.0,18)
 add_child(cam)
 cam.look_at(Vector3(0,1,-2))
 cam.current=true
 for i in range(9):
  box(self,Vector3(32,0.1,5),Vector3(0,-0.05,-18+i*5),Color(0.08,0.36,0.16) if i%2==0 else Color(0.1,0.45,0.2))
 var white=Color(0.9,0.94,0.94)
 box(self,Vector3(7.5,0.12,0.12),Vector3(0,2.44,-2.3),white)
 for side in [-1.0,1.0]:
  box(self,Vector3(0.12,2.44,0.12),Vector3(side*3.66,1.22,-2.3),white)
  box(self,Vector3(3.0,4.0,35),Vector3(side*19,2,-1),Color(0.11,0.14,0.2))
  for j in range(6):
   box(self,Vector3(2,0.3,35),Vector3(side*(19+j*0.3),0.5+j*0.5,-1),Color(0.13,0.2,0.29))
  var light=OmniLight3D.new()
  light.position=Vector3(side*19,12,-8)
  light.light_energy=2
  light.omni_range=35
  add_child(light)
 for i in range(13):
  box(self,Vector3(0.018,2.45,0.018),Vector3(-3.6+i*0.6,1.22,-3.7),Color(0.75,0.86,0.9))
 for i in range(6):
  box(self,Vector3(7.3,0.018,0.018),Vector3(0,i*0.45,-3.7),Color(0.75,0.86,0.9))
 for i in range(100):
  var x=-17.0+(i%50)*0.7
  var y=1.2+int(i/50)*0.6
  box(self,Vector3(0.3,0.5,0.3),Vector3(x,y,-23),Color(0.7,0.7,0.7) if i%2==0 else Color(0.2,0.2,0.25))
 keeper=Node3D.new()
 keeper.position=Vector3(0,0,-1)
 add_child(keeper)
 human(keeper,Color(0.94,0.75,0.1))
 shooter=Node3D.new()
 shooter.position=Vector3(0,0,12.0)
 add_child(shooter)
 human(shooter,COLORS[club])
 ball=Node3D.new()
 ball.position=Vector3(0,0.25,10)
 add_child(ball)
 sphere(ball,0.25,Vector3.ZERO,Color.WHITE)
 _ui()
func _button(parent:Control,label:String,fn:Callable)->Button:
 var b=Button.new()
 b.text=label
 b.custom_minimum_size=Vector2(280,60)
 b.add_theme_font_size_override("font_size",23)
 parent.add_child(b)
 b.pressed.connect(fn)
 return b
func _ui()->void:
 var layer=CanvasLayer.new()
 add_child(layer)
 var root=Control.new()
 root.set_anchors_preset(Control.PRESET_FULL_RECT)
 layer.add_child(root)
 title=Label.new()
 title.position=Vector2(25,20)
 title.add_theme_font_size_override("font_size",30)
 root.add_child(title)
 info=Label.new()
 info.position=Vector2(25,65)
 info.add_theme_font_size_override("font_size",22)
 root.add_child(info)
 var controls=HBoxContainer.new()
 controls.set_anchors_preset(Control.PRESET_BOTTOM_WIDE)
 controls.offset_top=-90
 root.add_child(controls)
 _button(controls,"MIRA ESQUERDA",func():aim=max(-1.0,aim-0.5);info.text="MIRA: "+str(aim))
 _button(controls,"MIRA DIREITA",func():aim=min(1.0,aim+0.5);info.text="MIRA: "+str(aim))
 _button(controls,"FORCA",func():power=0.3 if power>0.9 else power+0.2;info.text="FORCA: "+str(snapped(power,0.1)))
 _button(controls,"CHUTAR",_shoot)
 menu=ColorRect.new()
 menu.color=Color(0.01,0.03,0.06,0.96)
 menu.set_anchors_preset(Control.PRESET_FULL_RECT)
 root.add_child(menu)
 var column=VBoxContainer.new()
 column.position=Vector2(390,110)
 column.add_theme_constant_override("separation",15)
 menu.add_child(column)
 var heading=Label.new()
 heading.text="PENALTY 2027"
 heading.add_theme_font_size_override("font_size",60)
 heading.add_theme_color_override("font_color",Color(1,0.79,0.18))
 column.add_child(heading)
 _button(column,"JOGAR PENALTIS",func():menu.hide())
 _button(column,"TROCAR CLUBE",_next_club)
 _button(column,"CAMERA",_camera)
 _update()
func _update()->void:
 title.text="PENALTY 2027   |   "+CLUBS[club]+"   |   GOLS: "+str(score)+"   CHUTES: "+str(shots)
func _next_club()->void:
 club=(club+1)%CLUBS.size()
 for n in shooter.get_children():n.queue_free()
 human(shooter,COLORS[club])
 _update()
func _camera()->void:
 cam.position=Vector3(12,7,14) if cam.position.x==0 else Vector3(0,4,18)
 cam.look_at(Vector3(0,1,-2))
func _shoot()->void:
 if active:return
 active=true
 t=0
 shots+=1
 dest=Vector3(aim*3.15,0.7+power*1.65,-3.5)
 keeper_x=rng.randf_range(-2.8,2.8)
 goal=abs(dest.x-keeper_x)>0.9
 info.text="CHUTANDO..."
 _update()
func _process(delta:float)->void:
 if not active:return
 t+=delta
 var u=min(t/0.85,1.0)
 ball.position=Vector3(0,0.25,10).lerp(dest,u)
 ball.position.y+=sin(u*PI)*1.3
 ball.rotate_x(delta*12)
 keeper.position.x=lerp(0.0,keeper_x,u)
 keeper.rotation_degrees.z=-keeper.position.x*10
 shooter.position.z=12-u*1.4
 if t>1.1:
  active=false
  if goal:score+=1
  info.text="GOOOOL!" if goal else "DEFESA DO GOLEIRO!"
  _update()
  ball.position=Vector3(0,0.25,10)
  keeper.position=Vector3(0,0,-1)
  keeper.rotation=Vector3.ZERO
  shooter.position=Vector3(0,0,12)
