import 'package:flutter/material.dart';
import 'package:timetracker_app/PageIntervals.dart';
import 'package:timetracker_app/tree.dart' hide getTree;
import 'package:timetracker_app/requests.dart';
import 'dart:async';



class PageActivities extends StatefulWidget {

  int id;
  PageActivities(this.id);

  @override
  _PageActivitiesState createState() => _PageActivitiesState();
}

class _PageActivitiesState extends State<PageActivities> {
  late int id;
  late Future<Tree> futureTree;

  late Timer _timer;
  static const int periodeRefresh = 6;
  var _scaffoldKey = new GlobalKey<ScaffoldState>();

  void _activateTimer() {
    _timer = Timer.periodic(Duration(seconds: periodeRefresh), (Timer t) {
      futureTree = getTree(id);
      setState(() {});
    });
  }

  @override
  void dispose() {
    // "The framework calls this method when this State object will never build again"
    // therefore when going up
    _timer.cancel();
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    id = widget.id; // of PageActivities
    futureTree = getTree(id);
    _activateTimer();
  }



  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree>(
      future: futureTree,
      // this makes the tree of children, when available, go into snapshot.data
      builder: (context, snapshot) {
        // anonymous function
        if (snapshot.hasData) {
          String nomActivitat = snapshot.data!.root.name;
          if(snapshot.data!.root.name == "root")
          {
                nomActivitat = "Main Page";
          }
          return Scaffold(
            key: _scaffoldKey,
            appBar: AppBar(
              title: Text(nomActivitat), // updated 16-dec-2022
              actions: <Widget>[
                IconButton(icon: Icon(Icons.home),
                    onPressed: () {
                      while(Navigator.of(context).canPop()) {
                        print("pop");
                        Navigator.of(context).pop();
                      }
                      PageActivities(0);
                    }),
                //TODO other actions
              ],
            ),
            body: ListView.separated(
              // it's like ListView.builder() but better because it includes a separator between items
              padding: const EdgeInsets.all(16.0),
              itemCount: snapshot.data!.root.children.length, // updated 16-dec-2022
              itemBuilder: (BuildContext context, int index) =>
                  _buildRow(snapshot.data!.root.children[index], index, snapshot.data!.root.children), // updated 16-dec-2022
              separatorBuilder: (BuildContext context, int index) =>
              const Divider(),
            ),
            floatingActionButton: FloatingActionButton(
              heroTag: "btnAddTask",// Botó per afegir nova activitat
              onPressed: () { showDialog(
                context: context,
                builder: (_) {
                  var nameController = TextEditingController();
                  return AlertDialog(
                    title: Text("Inserir nova tasca"),
                    content:
                        TextFormField(
                          controller: nameController,
                          decoration: InputDecoration(labelText: "Nom Activitat", border: UnderlineInputBorder()),
                        ),

                    actions: [
                      TextButton(
                        onPressed: () {
                          add(snapshot.data!.root.id, 1, nameController.text);
                          Navigator.pop(context);
                          },
                        child: Text('Tasca', style: TextStyle(color: Colors.amberAccent),),
                      ),
                      TextButton(
                        onPressed: () {
                          add(snapshot.data!.root.id, 0, nameController.text);
                          Navigator.pop(context);
                        },
                        child: Text('Projecte', style: TextStyle(color: Colors.indigoAccent),),
                      ),
                      TextButton(
                        onPressed: () => Navigator.pop(context),
                        child: Text('Cancel·lar'),
                      )
                    ]
                  );
                }
              ); },
              tooltip: "Afegeix nova activitat",
              child: const Icon(Icons.add),
            ),
          );
        } else if (snapshot.hasError) {
          return Text("${snapshot.error}");
        }
        // By default, show a progress indicator
        return Container(
            height: MediaQuery.of(context).size.height,
            color: Colors.white,
            child: Center(
              child: CircularProgressIndicator(),
            ));
      },
    );
  }

  Widget _buildRow(Activity activity, int index, List<dynamic> children) {
    String strDuration = Duration(seconds: activity.duration).toString().split('.').first;
    // split by '.' and taking first element of resulting list removes the microseconds part
    if (activity is Project) {
      return ListTile(
        title: Text('${activity.name}  $strDuration'),
        leading: IconButton(
          icon: Icon(Icons.info),
          onPressed: () {_showmoreinfo(activity.initialDate.toString(), activity.finalDate.toString());},),
        //trailing: Text('$strDuration'),
        tileColor: activity.active ? Colors.green : Colors.indigoAccent, // Si és projecte, el fiquem en blau per diferenciar de tasca.
        onTap: () => _navigateDownActivities(activity.id),
      );
    } else {
      Task task = activity as Task;

      return ListTile(
        title: Text('${activity.name}  $strDuration'),
        trailing: FloatingActionButton(
          heroTag: "btnPlayPause",
          elevation: 0.0,
          backgroundColor: task.active ? Colors.greenAccent : Colors.amberAccent,
          child:task.active
              ? Icon(Icons.pause)
              : Icon(Icons.play_arrow),
          onPressed: (){
            if (task.active) {
              stop(activity.id);
              _refresh(); // to show immediately that task has started
            } else {
              //print(active);
              start(activity.id);
              _refresh(); // to show immediately that task has stopped
            }
          },
        ),
        leading: IconButton(
            icon: Icon(Icons.info),
            onPressed: () {_showmoreinfo(task.initialDate.toString(), task.finalDate.toString());},),
        tileColor: task.active ? Colors.greenAccent : Colors.amberAccent, // Si és tasca el fiquem en griset per diferenciar de projecte.
        onTap: () => _navigateDownIntervals(activity.id),
        /*onLongPress: () {
          print("ACTIVE TASK ? ");
          print(task.active);
          if (task.active) {
            stop(activity.id);

            _refresh(); // to show immediately that task has started
          } else {
            //print(active);
            start(activity.id);
            _refresh(); // to show immediately that task has stopped
          }
        },*/ // TODO start/stop counting the time for tis task
      );
    }

  }
/*  void _navigateDownActivities(int childId) {
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageActivities(childId),
    ));
  }

  void _navigateDownIntervals(int childId) {
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageIntervals(childId),
    ));
  }*/
 void _navigateDownActivities(int childId) {
    _timer.cancel();
    // we can not do just _refresh() because then the up arrow doesnt appear in the appbar
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageActivities(childId),
    )).then( (var value) {
      _activateTimer();
      _refresh();
    });
    //https://stackoverflow.com/questions/49830553/how-to-go-back-and-refresh-the-previous-page-in-flutter?noredirect=1&lq=1
  }

  void _navigateDownIntervals(int childId) {
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageIntervals(childId),
    )).then( (var value) {
      _activateTimer();
      _refresh();
    });
  }

  void _refresh() async {
    futureTree = getTree(id); // to be used in build()
    setState(() {});
  }
  void _showmoreinfo(String initialDate, String finalDate) {
    String delimiter = ".";
    int lastIndexInitial = initialDate.lastIndexOf(delimiter);
    int lastIndexFinal = finalDate.lastIndexOf(delimiter);
    String trimmedInitial = initialDate.substring(0, lastIndexInitial);
    String trimmedFinal = finalDate.substring(0, lastIndexFinal);
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text("More info"),
          content: Text("Initial Date: $trimmedInitial\n"
                        "Final Date: $trimmedFinal"),
          actions: [
            TextButton(
              child: Text("Back"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }
}