$execute as @e[type=!minecraft:player] run execute unless score @s count matches 1.. run apoli:power grantall @s $(powers)
execute as @e[type=!minecraft:player] run execute unless score @s count matches 1.. run scoreboard players set @s count 1
function catsorigins:storage