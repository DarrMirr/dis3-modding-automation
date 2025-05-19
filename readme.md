# Dis3-modding-automation tool

Disciples III modding automation tool

(For english readers, please read EN part of readme)

# RU:

Данный инструмент был разработан во время работы над [El Clasico модом](https://www.nexusmods.com/disciples3/mods/16)

## Цели инструмента

- автоматизировать рутинную работу моддера по выполнению однотипных действий
- облегчить работу моддера

## Системые требования

- Java 21 [скачать](https://adoptium.net/temurin/releases/?version=21&package=jdk&arch=x64)

## Запуск команды на выполнение

Dis3-modding-automation - утилита командной строки, поэтому для работы с ней вам нужен терминал (Linux) или командная строка cmd (Windows)

**Синтаксис запуска команды:**

Команду выполнять из директории, где расположен dis3-modding-automation.jar файл 

```bash
java -jar dis3-modding-automation_1.0.0.jar -command=имя_команды -опция1_команды=значение1 -опция2_команды=значение2
```

где 
- command - это обязательный параметр для всех команд.
- опцияN_команды - это специфичная для команды опция. Количество специфичных опций у каждой команды может быть разным.

## Поддерживаемые команды

1. **xls_to_character_stats**
    
   Описание: 
     - Конвертировать специальный файл в формате Excel с параматрами юнитов в файлы вида *.stat

   Параметры:
     - command=xls_to_character_stats
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/build/in work/balance/dis3_profiles.xlsx"
        - абсолютный путь от корневой директории диска до Excel файла

   Результат выполнения команды:
     - файлы *.stats в директории output, где лежит Excel файл
         

2. **character_stats_to_xls**

   Описание: 
     - Конвертировать игровые файлы вида *.stat в специальный файл в формате Excel. 

   Параметры:
     - command=character_stats_to_xls
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/decode/resources/Profiles/stats/*"
        - абсолютный путь от корневой директории диска до директории с игровыми файлами вида *.stat

   Результат выполнения команды:
     - файл Excel в директории output внутри директории, указанной в параметре source

3. **collect_model_files_into_dir**

   Описание: 
     - команда обходит дерево каталогов от корневой директории, указанной в параметре source и копирует все найденные в дочерних директориях *.g и *.a файлы в директорию, указанную в параметре target

   Параметры:
     - command=collect_model_files_into_dir
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/decode/resources/Characters/Empire/Leader-Archmage/"
        - абсолютный путь от корневой директории диска до директории из которой нужно извлечь *.g и *.a файлы

     - target="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/characters/empire/leader-archmage/"    
        - абсолютный путь от корневой директории диска до директории куда нужно сохранить результат работы команды

   Результат выполнения команды:
     - файлы *.g и *.a в директории, указанные в параметре target    

4. **accommodate_model_files_from_dir**

   Описание: 
     - команда раскладывает *.g и *.a файлы из директории, указанной в параметре source, в директории, которые следуют шаблону наименований директорий юнитов в игровых ресурсах игры

   Параметры:
     - command=accommodate_model_files_from_dir
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/characters/demons/"
        - абсолютный путь от корневой директории диска до директории где лежат *.g и *.a файлы

   Результат выполнения команды:
     - файлы *.g и *.a в директории, которая следует шаблону наименований директорий юнитов в игровых ресурсах игры

5. **print_area_zones**

   Описание: 
     - команда выводит на консоль псевдо-графическое отображение зон арены из hexmap.ini файла

   Параметры:
     - command=print_area_zones
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/hexmap.ini"
        - абсолютный путь от корневой директории диска до директории, где лежит hexmap.ini файл

   Результат выполнения команды:
     - в окне терминала (консоли командной строки) отобразится псевдо-графическое отображение зон арены из hexmap.ini файла

6. **copy_unit_arena_location**

   Описание: 
     - копирует координаты локаторов юнита из одной арена сцены в другие

   Параметры:
     - command=copy_unit_arena_location
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_land_empire/arena_back_land_empire_01_noon.scene"
        - абсолютный путь от корневой директории диска до файла, где лежит сцена арены - источник координат для локаторов юнитов.

     - target="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_land_empire/todo/*"
        - абсолютный путь от корневой директории диска до директории, где лежат сцены арены, куда нужно скопировать координаты для локаторов юнитов.        

   Результат выполнения команды:
     - сцены арены в директории output внутри директории, указанной в параметре target

7. **fix_pass_point_name_scene**

   Описание: 
     - исправляет имена hex объектов в файле (_p_ - для локаторов юнита игрока, _n_ - для локаторов npc, _с_ - центр арены, _0_ - остальные объекты). Это удобно, чтобы различать глазами в Toolkit разные типы локаторов.

   Параметры:
     - command=fix_pass_point_name_scene
        - имя команды

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_neutrals_town/temp/*"
        - абсолютный путь от корневой директории диска до директории, где лежат сцены арены, в которых необходимо осуществить правку наименований. 

   Результат выполнения команды:
     - сцены арены в директории output внутри директории, указанной в параметре source

8. **insert_child_scene_block**              

   Описание: 
     - аналог команды Merge scene из Toolkit. Отличается следующим:
       - можно указать родительский элемент, куда вставить блок сцены

   Параметры:
     - command=insert_child_scene_block
        - имя команды

     - source="/home/darrmirr/projects/java/darr_mirr/disciples3-utils/disciples3-utils/src/test/resources/block_to_insert.scene"
        - абсолютный путь от корневой директории диска до файла, где сохранён блок сцены.

     - parent="group \"Scene Root\""
        - имя блока куда нужно добавить вставляемый блок. Блок должен существовать в сцене, которая указывается в параметре target.         

     - target="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_land_empire/test/arena_back_land_empire_01_night.scene"
        - абсолютный путь от корневой директории диска до файла, где куда нужно вставить блок сцены.

   Результат выполнения команды:
     - сцена арены в директории output внутри директории, указанной в параметре target                            

# EN

Dis3-modding-automation tool has been developed for actions automatization on [El Clasico mod](https://www.nexusmods.com/disciples3/mods/16) works.


## Tool goals
- automate repeateable modding task
- simplify and speedup modding work

## System requirements

- Java 21 [download](https://adoptium.net/temurin/releases/?version=21&package=jdk&arch=x64)

## How to use tool

Dis3-modding-automation is command line utility. Therefore, you have to use whether terminal (on Linux) or cmd utility (on Windows) depends on Operation System (OS) do you use.

**How to launch tool to execute command:**

```bash
java -jar dis3-modding-automation_1.0.0.jar -command=predefined_command_name -command_option1=value1 -command_option2=value2
```

- command is mandatory argument for all tool invocations.
- command_optionN is dedicated option for particular command. Amount of command_options may be different from command to command.

## Predefined tool's commands

1. **xls_to_character_stats**

   Description: 
     - Convert special organized Excel file with unit profiles into *.stat game files.

   Arguments:
     - command=xls_to_character_stats
        - command name

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/build/in work/balance/dis3_profiles.xlsx"
        - absolute path to Excel file.

   Invocation result:
     - *.stats files in output directory aside to Excel source file.

2. **character_stats_to_xls**            

   Description: 
     - Convert *.stat game files into special organized Excel file with unit profiles

   Arguments:
     - command=character_stats_to_xls
        - command name

     - source="/home/darrmirr/downloads/disciples3/decode/resources/Profiles/stats/*"
        - absolute path to directory with *.stat game files in it.

   Invocation result:
     - special organized Excel file with unit profiles in output directory in source path directory.

3. **collect_model_files_into_dir**

   Description: 
     - command walk through directory files tree and copy all *.g, *.a files into target directory

   Arguments:
     - command=collect_model_files_into_dir
        - command name

     - source="/home/darrmirr/downloads/disciples3/decode/resources/Characters/Empire/Leader-Archmage/"
        - absolute path to directory with *.g, *.a game files in it.

     - target="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/characters/empire/leader-archmage/"    
        - absolute path to directory where to place files as command execution results.

   Invocation result:
     - *.g, *.a game files are copyied from source directory into target one.        

4. **accommodate_model_files_from_dir**

   Description: 
     - command creates direcroty files tree and move *.g, *.a files at particular places as game expected to.

   Arguments:
     - command=accommodate_model_files_from_dir
        - command name

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/characters/demons/"
        - absolute path to directory with *.g, *.a game files in it.

   Invocation result:
     - *.g, *.a game files at dedicated directory as game expected to.

5. **print_area_zones**

   Description: 
     - command print out to console at textual view arena zone areas are described at hexmap.ini file.

   Arguments:
     - command=print_area_zones
        - command name

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/hexmap.ini"
        - absolute path to hexmap.ini file.

   Invocation result:
     - textual picture of arena zone areas are described at hexmap.ini file.

6. **copy_unit_arena_location**

   Description: 
     - command copy unit arena locator coords from source file to target ones.

   Arguments:
     - command=copy_unit_arena_location
        - command name

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_land_empire/arena_back_land_empire_01_noon.scene"
        - absolute path to source *.scene file.

     - target="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_land_empire/todo/*"
        - absolute path to directory with target files in it.

   Invocation result:
     - output directory in target one contains files with changed unit arena locator coords.

7. **fix_pass_point_name_scene**

   Description: 
     - fix hex object name in *.scene file. (_p_ - for player unit locator, _n_ - for npc unit locator, _c_ - arena center, _0_ - all another hex objects)

   Arguments:
     - command=fix_pass_point_name_scene
        - command name

     - source="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_neutrals_town/temp/*"
        - absolute path to source *.scene file.



   Invocation result:
     - output directory in source one contains *.scene files with changed hex object names.

8. **insert_child_scene_block** 

   Description: 
     - this command is quite the same as "Merge scene" from game's Toolkit. But there is a difference:
       - this command insert scene block as child one. You have to set parent block name.

   Arguments:
     - command=insert_child_scene_block
        - command name

     - source="/home/darrmirr/projects/java/darr_mirr/disciples3-utils/disciples3-utils/src/test/resources/block_to_insert.scene"
        - absolute path to source *.scene file.

     - parent="group \"Scene Root\""
        - Parent block name. Block must exist in *.scene file that set at target parameter.

       - target="/home/darrmirr/downloads/disciples3/mods/el_clasico/project/internal/resources/hexagons/models/arena/arena_back_land_empire/test/arena_back_land_empire_01_night.scene"
        - absolute path to target *.scene file.

   Invocation result:
     - modified copy of *.scene file that set at target parameter.