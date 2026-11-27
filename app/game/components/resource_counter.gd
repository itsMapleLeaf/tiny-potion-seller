@tool
class_name ResourceCounter
extends Control

@export
var icon_texture: Texture2D = null:
	set(value):
		icon_texture = value
		_update()

@export
var text := "":
	set(value):
		text = value
		_update()

@export
var font_size := 24:
	set(value):
		font_size = value
		_update()

@export_range(8, 256, 2, "or_greater")
var icon_size := 32.0:
	set(value):
		icon_size = value
		_update()


@onready var icon: TextureRect = %Icon
@onready var label: Label = %Label

func _ready() -> void:
	_update()

func _update() -> void:
	if not is_node_ready(): return

	icon.texture = icon_texture

	label.text = text

	icon.custom_minimum_size.x = icon_size
	icon.custom_minimum_size.y = icon_size

	label.add_theme_font_size_override("font_size", font_size)
