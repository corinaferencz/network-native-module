import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  View,
  FlatList,
  NativeModules,
} from 'react-native';
import 'react-native-get-random-values';
import { v4 as uuidv4 } from 'uuid';

const { NetworkModule } = NativeModules;

const Item = ({ title }) => (
  <View style={styles.item}>
    <Text style={styles.title}>{title}</Text>
  </View>
);

const App = () => {

  let [books, setBooks] = useState([]);

  useEffect( () => { 
    async function fetchBooks() {
      try {
        const fetchedBooks = await NetworkModule.getBooks();
        const mappedBooks = fetchedBooks.map((book) => ({id: uuidv4(), title: book }));
        setBooks(mappedBooks);  
      } catch (e) {
        console.error(e)
      }
    }
    fetchBooks();
}, []);

const renderItem = ({ item }) => (
  <Item title={item.title}/>
);

  return (
    <SafeAreaView style={styles.container}>
      <FlatList
        data={books}
        renderItem={renderItem}
        keyExtractor={item => item.id}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: StatusBar.currentHeight || 0,
  },
  item: {
    backgroundColor: '#f1802d',
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16,
  },
  title: {
    fontSize: 32,
  },
});

export default App;