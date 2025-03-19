import React, { useState, useEffect } from 'react';
import { Search as SearchIcon, Home, MapPin, X } from 'lucide-react';

const Test = () => {
    const [posts, setPosts] = useState([]);
    useEffect(() => {
       fetch('https://jsonplaceholder.typicode.com/posts?_limit=10')
          .then((response) => response.json())
          .then((data) => {
             console.log(data);
             setPosts(data);
          })
          .catch((err) => {
             console.log(err.message);
          });
    }, []);
  return (

    <div className="posts-container">
      {posts.map((post) => {
         return (
            <div className="post-card" key={post.id}>
               <h2 className="post-title">Title: {post.title}</h2>
               <image ></image>
               <p className="post-body">Body: {post.body}</p>
               <div className="button">
               <button className="delete-btn">Delete</button>
               </div>
            </div>
         );
      })}
   </div>
  );
};

export default Test;