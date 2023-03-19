// api.js


import axios from 'axios';
import { getAccessToken } from './redux/slices/auth';


// const accessToken = getAccessToken();
// console.log("accessToken: ", accessToken);

// const getAuthHeader = () => {
//   const accessToken = localStorage.getItem('accessToken');
//   if (accessToken) {
//     return { Authorization: `Bearer ${accessToken}` };
//   }
//   return {};
// };


export const getPosts = async ({ postIDStart, count, reverse }) => {
  try {
    const response = await axios.get('http://localhost:8080/getPosts', {
      params: {
        postIDStart,
        count,
        reverse,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const getPost = async ({postID}) => {
    console.log("getReq: ", postID);
    const response = await axios.get('http://localhost:8080/getPost', {
      params: { postID },
    });
    return response.data;
};


export const createPost = async ({ title, content, allowComments }) => {
  const token = getAccessToken();
  console.log("createPost token: ", token);
  console.log("typeof token.credential: ", typeof token.credential);
  // const cred = (token.credential);
  // console.log("createPost credential: ", cred);
  const response = await axios.post('http://localhost:8080/createPost', {
    title,
    content,
    allowComments,
  }, {
    headers: {
      Authorization: `Bearer ${token.credential}`,
      'X-Oauth-Provider': 'google',
      'X-Oauth-Credential': JSON.stringify(token.credential),
    },
  });
  return response.data;
};




//Comments
export const getComments = async ({ postID, commentIDStart, count, reverse }) => {
    try {
      const response = await axios.get('http://localhost:8080/getComments', {
        params: {
          postID,
          commentIDStart,
          count,
          reverse,
        },
      });
      return response.data;
    } catch (error) {
      throw new Error(error.message);
    }
};

export const getComment = async ({postID, commentID}) => {
    //console.log("getReq: ", postID);
    const response = await axios.get('http://localhost:8080/getComment', {
      params: { postID, commentID },
    });
    return response.data;
};

export const createComment = async ({ postID, authorID, content }) => {
    const response = await axios.post('http://localhost:8080/createComment', {
      postID,
      authorID,
      content,
    });
    return response.data;
};

export const upvotePost = async ({postID}) => {
  console.log("upvotePost: ", postID);
  try {
    const response = await axios.put('http://localhost:8080/upvotePost', JSON.stringify({ postID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const downvotePost = async ( {postID} ) => {
  try {
    const response = await axios.put('http://localhost:8080/downvotePost', JSON.stringify({ postID: postID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const upvoteComment = async ({ postID, commentID }) => {
  try {
    console.log("upvoteComment: ", postID, commentID);
    const response = await axios.put('http://localhost:8080/upvoteComment', JSON.stringify({ postID: postID, commentID: commentID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const downvoteComment = async ({ postID, commentID }) => {
  try {
    const response = await axios.put('http://localhost:8080/downvoteComment', JSON.stringify({ postID: postID, commentID: commentID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const deleteComment = async ({ postID, commentID }) => {
  try {
    const body = JSON.stringify({ postID, commentID });
    const response = await axios.delete('http://localhost:8080/deleteComment', {
      data: body,
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const editComment = async ({ postID, commentID, content }) => {
  try {
    const response = await axios.put('http://localhost:8080/editComment', JSON.stringify({ postID: postID, commentID: commentID, content: content }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};
